import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {

  val liftVersion = "2.4-M1"

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion,
    "net.liftweb" %% "lift-mongodb-record" % liftVersion,
    "net.liftweb" %% "lift-testkit" % liftVersion,
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" %% "specs" % "1.6.8" % "test->default"
  ) ++ super.libraryDependencies
}
