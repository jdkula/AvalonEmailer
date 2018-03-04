import Roles.assassin
import Roles.merlin
import Roles.mordred
import Roles.morgana
import Roles.percival
import Roles.servantOfArthur

object Constants {
    val smtpEmail: String = System.getenv("AVALON_EMAIL") ?: throw Error("No Email in Environment!")
    val smtpPassword: String = System.getenv("AVALON_PASSWORD") ?: throw Error("No Password in Environment!")
    const val smtpHost: String = "smtp.gmail.com"
    const val smtpPort: Int = 587

    const val emailSubject: String = "Avalon Selection!"
}