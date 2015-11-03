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
  "com.h2database"      %  "h2"                     % "1.4.187",
  "jp.t2v"              %% "play2-auth"             % "0.14.1",
  "jp.t2v"              %% "play2-auth-test"        % "0.14.1" % "test",
  "jp.t2v"              %% "stackable-controller"   % "0.5.0",
  specs2 % Test
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

