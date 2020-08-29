package dev.ostrander.musicquiz

import ackcord.commands.CommandController
import ackcord.commands.MessageParser.RemainingAsString
import ackcord.commands.NamedCommand
import ackcord.requests.Requests
import akka.actor.typed.ActorRef
import dev.ostrander.musicquiz.actor.GameManager

trait Commands {
  def apply(): List[NamedCommand[RemainingAsString]]
}

object Commands {
  def apply(
    requests: Requests,
    gameActor: ActorRef[GameManager.Command],
  ): List[NamedCommand[RemainingAsString]] = {
    val controller: Commands = new CommandController(requests) with Commands {
      val game: NamedCommand[RemainingAsString] =
        GuildVoiceCommand.named(Seq("!"), Seq("start"), mustMention = false).parsing[RemainingAsString].withSideEffects { r =>
          gameActor ! GameManager.CreateGame(r.textChannel, r.voiceChannel)
        }

      val reset: NamedCommand[RemainingAsString] =
        GuildCommand.named(Seq("!"), Seq("reset"), mustMention = false).parsing[RemainingAsString].withSideEffects { r =>
          gameActor ! GameManager.Reset(r.textChannel)
        }

      override def apply(): List[NamedCommand[RemainingAsString]] = game :: reset :: Nil
    }
    controller()
  }
}
