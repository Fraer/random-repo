logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Typesafe repository plugin" at "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.4")