import Constants.emailSubject
import Constants.smtpEmail
import Constants.smtpPassword
import Roles.knowsMore
import com.google.common.primitives.Longs
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import java.security.SecureRandom
import java.util.*

fun main(args: Array<String>) {
    assert(GameConfiguration.players.size == GameConfiguration.selection.size)
    assert(GameConfiguration.players.isNotEmpty())
    val shuffledPlayers = GameConfiguration.players.shuffled(SecureRandom(Longs.toByteArray(Calendar.getInstance().timeInMillis)))

    val possibleRoles = GameConfiguration.selection

    val firstPlayer = shuffledPlayers[0]

    val playerMap = HashMap<Player, Role>()
    for (i in shuffledPlayers.indices) {
        playerMap += shuffledPlayers[i] to GameConfiguration.selection[i]
    }

    println("Sending emails to bad players...")
    val badPlayers = playerMap.filter { it.value !is Good && it.value !== Roles.oberon }
    for (badPlayer in badPlayers.keys)
        sendMail(
                """Your role, $badPlayer, is ${playerMap[badPlayer]}.
                    |The other evil players are ${badPlayers.filterKeys { it !== badPlayer }.keys}.
                    |${firstMessage(badPlayer, firstPlayer)}
                    |Possible roles are $possibleRoles.""".trimMargin(),
                badPlayer.email
        )

    println("Sending an email to Merlin...")
    val merlinKnows = playerMap.filter { it.value !is Good && it.value !== Roles.mordred }
    val merlinMaybe = playerMap.filter { it.value === Roles.merlin }
    for (merlin in merlinMaybe.keys)
        sendMail(
                """Your role, $merlin, is ${playerMap[merlin]}.
                    |${merlinKnows.keys} are all evil!
                    |${firstMessage(merlin, firstPlayer)}
                    |Possible roles are $possibleRoles.""".trimMargin(),
                merlin.email
        )

    println("Sending an email to Percival...")
    val percivalKnows = playerMap.filter { it.value === Roles.morgana || it.value === Roles.merlin }
    val percivalMaybe = playerMap.filter { it.value === Roles.percival }
    for (percival in percivalMaybe.keys)
        sendMail(
                """Your role, $percival, is ${playerMap[percival]}.
                    |Merlin is one of ${percivalKnows.keys}!
                    |${firstMessage(percival, firstPlayer)}
                    |Possible roles are $possibleRoles.
                """.trimMargin(),
                percival.email
        )

    println("Sending the rest of the emails...")
    val theRest = playerMap.filterNot { it.value.knowsMore() }
    for (player in theRest.keys)
        sendMail(
                """Your role, $player, is ${playerMap[player]}.
                    |${firstMessage(player, firstPlayer)}
                    |Possible roles are $possibleRoles.
                """.trimMargin(),
                player.email
        )
}

fun firstMessage(player: Player, firstPlayer: Player): String =
        if (player === firstPlayer)
            "You're First!"
        else
            ""

fun sendMail(message: String, destination: String) {
    val email = SimpleEmail()
    email.hostName = Constants.smtpHost
    email.setSmtpPort(Constants.smtpPort)
    email.setAuthenticator(DefaultAuthenticator(smtpEmail, smtpPassword))
    email.isTLS = true
    email.setFrom(smtpEmail)
    email.addTo(destination)
    email.subject = emailSubject
    email.setMsg(message)
    email.send()
}