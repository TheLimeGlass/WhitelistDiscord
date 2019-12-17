# WhitelistDiscord (1.15 last compiled Spigot)
A Minecraft plugin that allows Whitelisting via Discord

## Compiling/Setup

1.) change directories `cd PATH` replace path with your path.

2.) `git clone https://github.com/TheLimeGlass/WhitelistDiscord`

3.) Make a Discord bot application at https://discordapp.com/developers/applications make it a bot to then get it's bot token, you will need to add that to the config.yml in step 7.

4.) Make a Discord text channel called `minecraft` (must be named that), the bot will use that channel use that as the channel id. (Google how to get Discord channels ID's if unknown)

5.) `gradle build` if you have gradle installed or `./gradlew build` on unix devices, `gradlew build` works for windows.

6.) Compiled version will be in /build/lib/

7.) Drop jar into plugins folder on your Spigot Minecraft server, run server, configure config.yml with credentials and restart server. Enjoy!
