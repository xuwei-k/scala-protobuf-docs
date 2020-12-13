import sbt._
import tut.TutPlugin.autoImport._
import scala.sys.process.Process

object Honkit extends NpmCliBase {
  val honkitBin = nodeBin / "honkit"

  sealed trait Format {def command: String}
  object Format {
    case object Html extends Format { def command = "build" }
    case object Epub extends Format { def command = "epub" }
    case object Pdf extends Format { def command = "pdf" }
  }

  def buildBook(format: Format) = Def.inputTask[Unit] {
    val options = rawStringArg("<honkit command>").parsed
    val command = s"$honkitBin  ${format.command} $bookBuildDir $options"
    printRun(Process(command))
  }

  lazy val helpHonkit = taskKey[Unit]("help Honkit")
  lazy val build = taskKey[Unit]("build Honkit to html (an alias of html)")
  lazy val html = inputKey[Unit]("build Honkit to html")
  lazy val epub = inputKey[Unit]("build Honkit to epub")
  lazy val buildAll = taskKey[Unit]("build Honkit to all format")

  val settings = Seq(
    helpHonkit := printRun(Process(s"$honkitBin help")),
    html := buildBook(Format.Html).dependsOn(tut).evaluated,
    build := html.toTask("").value,
    epub := buildBook(Format.Epub).dependsOn(tut).evaluated,
    buildAll := epub.toTask("").dependsOn(html.toTask("")).value
  )
}
