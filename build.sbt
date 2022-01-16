name := "ISBD_backend"

version := "1.0"

lazy val `playtest` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"

libraryDependencies += "org.squeryl" %% "squeryl" % "0.9.16"
libraryDependencies += "org.postgresql" % "postgresql" % "42.3.1"




