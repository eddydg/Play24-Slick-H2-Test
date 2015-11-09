name := "PlayTest24"

version := "1.0"

lazy val `playtest24` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  /*jdbc,*/ // user slick
  /*evolutions,*/ // use slick evolutions
  cache,
  filters,
  ws,
  "com.typesafe.play"   %% "play-slick"             % "1.0.0",
  "com.typesafe.play"   %% "play-slick-evolutions"  % "1.0.0",
  /*"com.typesafe.slick"  %% "slick-codegen"          % "3.1.0",*/
  "com.h2database"      %  "h2"                     % "1.4.187",
  "jp.t2v"              %% "play2-auth"             % "0.14.1",
  "jp.t2v"              %% "play2-auth-test"        % "0.14.1" % "test",
  "jp.t2v"              %% "stackable-controller"   % "0.5.0",
  specs2 % Test
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

/*
lazy val slick = TaskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
  val url = "jdbc:h2:mem:play;INIT=runscript from 'conf/evolutions/default/1.sql'" // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
  val jdbcDriver = "org.h2.Driver"
  val slickDriver = "slick.driver.H2Driver"
  val pkg = "demo"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
  val fname = outputDir + "/demo/Tables.scala"
  Seq(file(fname))
}*/
