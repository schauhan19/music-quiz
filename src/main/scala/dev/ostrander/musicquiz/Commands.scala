package dev.ostrander.musicquiz

import ackcord.commands.CommandController
import ackcord.commands.MessageParser.RemainingAsString
import ackcord.commands.NamedCommand
import ackcord.requests.Requests
import ackcord.syntax.TextChannelSyntax
import akka.actor.typed.ActorRef
import dev.ostrander.musicquiz.actor.GameManager

class Commands(
  requests: Requests,
  gameActor: ActorRef[GameManager.Command],
) extends CommandController(requests) {
  val game: NamedCommand[RemainingAsString] =
    GuildVoiceCommand.named("!", Seq("start"), mustMention = false).parsing[RemainingAsString].withSideEffects { r =>
      gameActor ! GameManager.CreateGame(r.textChannel, r.voiceChannel)
    }
   def ratelimitTest(name: String): NamedCommand[Int] =
    Command.named("!", Seq(name), mustMention = false).parsing[Int].withSideEffects { m =>
      List.tabulate(m.parsed)(i => m.textChannel.sendMessage(s"Msg$i")).foreach(requests.singleIgnore)
    }
}
