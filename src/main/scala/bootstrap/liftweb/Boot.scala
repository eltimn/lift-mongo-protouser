package bootstrap
package liftweb

import java.util.Locale
import javax.mail.{Authenticator, PasswordAuthentication}

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.provider._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._
import net.liftweb.mongodb._

import Helpers._

import code.model._

/**
* A class that's instantiated early and run.  It allows the application
* to modify lift's environment
*/
class Boot extends Loggable {
  def boot {
    MongoDB.defineDb(
      DefaultMongoIdentifier,
      MongoAddress(MongoHost(), "lift_app")
    )

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = Menu(Loc("Home", List("index"), "Home")) ::
    Menu(Loc("Static", Link(List("static"), true, "/static/index"), 
      "Static Content")) ::
    User.sitemap

    LiftRules.setSiteMap(SiteMap(entries:_*))

    /*
    * Show the spinny image when an Ajax call starts
    */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
    * Make the spinny image go away when it ends
    */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    LiftRules.loggedInTest = Full(() => User.loggedIn_?)
    
    LiftRules.localeCalculator = localeCalculator _
    
    // config an email sender
    configMailer
  }

  /**
  * Force the request to be UTF-8
  */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
  
  private def localeCalculator(request : Box[HTTPRequest]): Locale =
    User.currentUser.map(u => new Locale(u.locale.value)) openOr Locale.getDefault
    
  /*
  * Config mailer
  */
  private def configMailer {

    var isAuth = Props.get("mail.smtp.auth", "false").toBoolean

    Mailer.customProperties = Props.get("mail.smtp.host", "localhost") match {
      case "smtp.gmail.com" =>
        isAuth = true
        Map(
          "mail.smtp.host" -> "smtp.gmail.com",
          "mail.smtp.port" -> "587",
          "mail.smtp.auth" -> "true",
          "mail.smtp.starttls.enable" -> "true"
        )
      case h => Map(
        "mail.smtp.host" -> h,
        "mail.smtp.port" -> Props.get("mail.smtp.port", "25"),
        "mail.smtp.auth" -> isAuth.toString
      )
    }

    if (isAuth) {
      (Props.get("mail.smtp.user"), Props.get("mail.smtp.pass")) match {
        case (Full(username), Full(password)) =>
          Mailer.authenticator = Full(new Authenticator() {
            override def getPasswordAuthentication = new
              PasswordAuthentication(username, password)
          })
        case _ => logger.error("Username/password not supplied for Mailer.")
      }
    }
  }
}
