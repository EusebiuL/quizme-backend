import sbt._

lazy val root = Project(id = "quizme", base = file("."))
.settings(commonSettings)
.aggregate(
  `quiz-server`
)

lazy val `quiz-server` = project
  .settings(commonSettings)
.settings(sbtAssemblySettings)
.settings(
  mainClass := Option("quiz.server.QuizServerApp")
)
  .dependsOn(
    `quiz-core`,
    `quiz-config`,
    `organizer-user`
  )
  .aggregate(
    `quiz-core`,
    `quiz-config`,
    `organizer-user`
  )

lazy val `quiz-core` = project
  .settings(commonSettings)
  .settings(sbtAssemblySettings)
  .settings(libraryDependencies ++=Seq(
    linebacker,
    catsCore,
    shapeless,
    log4cats,
    logbackClassic,
    spire,
    circeCore,
    circeGeneric,
    circeGenericExtras,
    http4sCirce,
    specs2,
    upperbound,
    http4sBlazeClient,
    http4sBlazeServer,
    http4sDSL
    )
  )

lazy val `quiz-config` = project
  .settings(commonSettings)
  .settings(libraryDependencies ++=Seq(
    pureConfig,
    bmcEffects
    )
  )
  .dependsOn(
    `quiz-core`
  )

lazy val `quiz-json` = project
  .settings(commonSettings)
  .settings(libraryDependencies ++=Seq(
    bmcEffects,
    bmcJson
  )
  )
  .dependsOn(
    `quiz-core`,
    `quiz-effects`
  )

lazy val `quiz-effects` = project
  .settings(commonSettings)
  .settings(sbtAssemblySettings)
  .settings(
    libraryDependencies ++= Seq(
      catsEffect,
      bmcEffects,
      monix,
      fs2,
    )
  )

lazy val `quiz-http` = project
  .settings(commonSettings)
  .settings(sbtAssemblySettings)
  .dependsOn(
    `quiz-core`,
    `quiz-json`,
    `quiz-effects`
  )

lazy val `quiz-db` = project
  .settings(commonSettings)
  .settings(sbtAssemblySettings)
  .settings(
    libraryDependencies ++= Seq(
      mongoDriver,
      bson,
      mongoDriverAsync,
      mongoDriverCore,
      mongoScalaBson,
      mongoReactiveDriver
    )
  )
  .dependsOn(
    `quiz-config`
  )

lazy val `algebra-user` = project
  .settings(commonSettings)
  .settings(sbtAssemblySettings)
  .dependsOn(
    `quiz-db`,
    `quiz-core`,
    `quiz-effects`
  )

lazy val `algebra-auth` = project
  .settings(commonSettings)
  .settings(sbtAssemblySettings)
  .dependsOn(
   `algebra-user`
  )

lazy val `organizer-user` = project
  .settings(commonSettings)
  .settings(sbtAssemblySettings)
  .dependsOn(
    `algebra-auth`,
    `quiz-json`,
    `quiz-http`
  )

def commonSettings: Seq[Setting[_]] = Seq(
  scalaVersion := "2.12.6",
  parallelExecution in Test := false,
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),
  scalacOptions ++= customScalaCompileFlags,
  dependencyOverrides += "org.typelevel" %% "cats-core"   % catsVersion,
  dependencyOverrides += "org.typelevel" %% "cats-effect" % catsEffectVersion,
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

def sbtAssemblySettings: Seq[Setting[_]] = {
  import sbtassembly.MergeStrategy
  import sbtassembly.PathList

  baseAssemblySettings ++
    Seq(
      // Skip tests during while running the assembly task
      test in assembly := {},
      assemblyMergeStrategy in assembly := {
        case PathList("application.conf", _ @_*) => MergeStrategy.concat
        case "application.conf" => MergeStrategy.concat
        case PathList("reference.conf", _ @_*) => MergeStrategy.concat
        case "reference.conf" => MergeStrategy.concat
        case x                => (assemblyMergeStrategy in assembly).value(x)
      },
      //this is to avoid propagation of the assembly task to all subprojects.
      //changing this makes assembly incredibly slow
      aggregate in assembly := false
    )
}

def customScalaCompileFlags: Seq[String] = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-Yrangepos",
  "-explaintypes", // Explain type errors in more detail.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
  "-language:higherKinds", // Allow higher-kinded types
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfuture", // Turn on future language features.
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Xlint:unsound-match", // Pattern match may not be typesafe.
  "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard",  // Warn when non-Unit expression results are unused.
  "-Ypartial-unification", // Enable partial unification in type constructor inference

  //"-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
  /*
   * These are flags specific to the "better-monadic-for" plugin:
   * https://github.com/oleg-py/better-monadic-for
   */
  "-P:bm4:no-filtering:y",
  "-P:bm4:no-map-id:y",
  "-P:bm4:no-tupling:y"
)

//https://github.com/busymachines/busymachines-commons
def bmCommons(m: String): ModuleID = "com.busymachines" %% s"busymachines-commons-$m" % "0.3.0-RC8"

lazy val bmcCore:          ModuleID = bmCommons("core")              withSources ()
lazy val bmcDuration:      ModuleID = bmCommons("duration")          withSources ()
lazy val bmcEffects:       ModuleID = bmCommons("effects")           withSources ()
lazy val bmcEffectsSync:   ModuleID = bmCommons("effects-sync")      withSources ()
lazy val bmcEffectsSyncC:  ModuleID = bmCommons("effects-sync-cats") withSources ()
lazy val bmcEffectsAsync:  ModuleID = bmCommons("effects-async")     withSources ()
lazy val bmcJson:          ModuleID = bmCommons("json")              withSources ()
lazy val bmcSemVer:        ModuleID = bmCommons("semver")            withSources ()
lazy val bmcSemVerParsers: ModuleID = bmCommons("semver-parsers")    withSources ()

lazy val catsVersion = "1.5.0"
//https://github.com/typelevel/cats
lazy val catsCore: ModuleID = "org.typelevel" %% "cats-core" % catsVersion withSources ()

lazy val catsEffectVersion = "1.2.0"
//https://github.com/typelevel/cats-effect
lazy val catsEffect: ModuleID = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources ()

//https://github.com/monix/monix
lazy val monix: ModuleID = "io.monix" %% "monix" % "3.0.0-RC1" withSources ()

lazy val fs2Version = "1.0.3"
//https://github.com/functional-streams-for-scala/fs2
lazy val fs2: ModuleID = "co.fs2" %% "fs2-core" % fs2Version withSources ()

//https://circe.github.io/circe/
lazy val circeVersion: String = "0.11.0"

lazy val circeCore:          ModuleID = "io.circe" %% "circe-core"           % circeVersion
lazy val circeGeneric:       ModuleID = "io.circe" %% "circe-generic"        % circeVersion
lazy val circeGenericExtras: ModuleID = "io.circe" %% "circe-generic-extras" % circeVersion

//https://github.com/http4s/http4s
lazy val Http4sVersion = "0.20.0-M5"
lazy val http4sBlazeServer: ModuleID = "org.http4s" %% "http4s-blaze-server" % Http4sVersion withSources ()
lazy val http4sBlazeClient: ModuleID = "org.http4s" %% "http4s-blaze-client" % Http4sVersion withSources ()
lazy val http4sCirce:       ModuleID = "org.http4s" %% "http4s-circe"        % Http4sVersion withSources ()
lazy val http4sDSL:         ModuleID = "org.http4s" %% "http4s-dsl"          % Http4sVersion withSources ()
lazy val http4sWebSockets:  ModuleID = "org.http4s" %% "http4s-websocket"    % Http4sVersion withSources ()


//http://mongodb.github.io/mongo-scala-driver/
lazy val mongoDriver: ModuleID = "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0" withSources()
lazy val bson: ModuleID = "org.mongodb" % "bson" % "3.10.1" withSources()
lazy val mongoScalaBson: ModuleID = "org.mongodb.scala" %% "mongo-scala-bson" % "2.6.0" withSources()
lazy val mongoDriverCore: ModuleID =  "org.mongodb" % "mongodb-driver-core" % "3.10.1" withSources()
lazy val mongoDriverAsync: ModuleID = "org.mongodb" % "mongodb-driver-async" % "3.10.1" withSources()
lazy val mongoReactiveDriver: ModuleID = "org.mongodb" % "mongodb-driver-reactivestreams" % "1.11.0" withSources()

lazy val shapeless: ModuleID = "com.chuusai" %% "shapeless" % "2.3.3" withSources ()

//============================================================================================
//==========================================  math ===========================================
//============================================================================================

lazy val spire: ModuleID = "org.typelevel" %% "spire" % "0.14.1" withSources ()

//============================================================================================
//========================================  security  ========================================
//============================================================================================

//https://github.com/jmcardon/tsec
lazy val tsecV = "0.0.1-M11"

lazy val tsec = Seq(
  "io.github.jmcardon" %% "tsec-common"        % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-password"      % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-cipher-jca"    % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-cipher-bouncy" % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-mac"           % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-signatures"    % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-hash-jca"      % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-hash-bouncy"   % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-libsodium"     % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-jwt-mac"       % tsecV withSources (),
  "io.github.jmcardon" %% "tsec-jwt-sig"       % tsecV withSources (),
)

//============================================================================================
//=========================================  logging =========================================
//============================================================================================
//https://github.com/ChristopherDavenport/log4cats
lazy val log4cats = "io.chrisdavenport" %% "log4cats-slf4j" % "0.2.0" withSources ()

//this is a Java library, notice that we used one single % instead of %%
//it is the backend implementation used by log4cats
lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3" withSources ()

//============================================================================================
//==========================================  email ==========================================
//============================================================================================

//this is a Java library, notice that we used one single % instead of %%
lazy val javaxMail = "com.sun.mail" % "javax.mail" % "1.6.1" withSources ()

//============================================================================================
//=========================================  testing =========================================
//============================================================================================

//https://github.com/etorreborre/specs2
lazy val specs2: ModuleID = "org.specs2" %% "specs2-core" % "4.3.0" % Test withSources ()

//============================================================================================
//=========================================== misc ===========================================
//============================================================================================

//https://github.com/pureconfig/pureconfig
lazy val pureConfig: ModuleID = "com.github.pureconfig" %% "pureconfig" % "0.9.1" withSources ()

//https://github.com/ChristopherDavenport/linebacker
lazy val linebacker: ModuleID = "io.chrisdavenport" % "linebacker_2.12" % "0.2.0" withSources ()

//https://github.com/pathikrit/better-files
lazy val betterFiles: ModuleID = "com.github.pathikrit" % "better-files_2.12" % "3.6.0" withSources ()

lazy val amazonSdkS3: ModuleID = "com.amazonaws" % "aws-java-sdk-s3" % "1.11.396" withSources ()

//https://github.com/SystemFw/upperbound
lazy val upperbound: ModuleID = "org.systemfw" %% "upperbound" % "0.2.0-M2" withSources()

