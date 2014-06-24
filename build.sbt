organization := "shapelessexperiments"

name := "default"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.0.0"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfuture")

scalacOptions ++= Seq("-Xlint")

EclipseKeys.withSource in ThisBuild := true

initialCommands += """import shapeless._
import shapelessexperiments._
import TestGeneric._"""

