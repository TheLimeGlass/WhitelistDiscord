# WhitelistDiscord (1.15 last compiled Spigot)
A Minecraft plugin that allows Whitelisting via Discord

## Compiling/Setup

1.) change directories `cd PATH` replace path with your path.

2.) `git clone https://github.com/TheLimeGlass/WhitelistDiscord`

3.) Make a Discord text channel called `minecraft`, the bot will use that channel use that as the channel id. (Google how to get Discord channels ID's if unknown)

4.) `gradle build` if you have gradle installed or `./gradlew build` on unix devices, `gradlew build` works for windows.

6.) Compiled version will be in /build/lib/

7.) Drop jar into plugins folder on your Spigot Minecraft server, run server, configure config.yml with credentials and restart server. Enjoy!
