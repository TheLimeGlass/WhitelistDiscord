# WhitelistDiscord (1.19 last compiled Spigot)
A Minecraft plugin that allows Whitelisting via Discord

## Download Setup

1.) Jar download is at https://github.com/TheLimeGlass/WhitelistDiscord/releases

3.) Make a Discord bot application at https://discordapp.com/developers/applications make it a bot to then get it's bot token, you will need to add that to the config.yml in step 7. Discord also has a bot invite link generator on the same page as of writting this, you can use that to invite your newly made bot to your server (You must have admin permissions on guild).

4.) Make a Discord text channel called `minecraft` (must be named that), the bot will use that channel, grab the channel id you will need it in step 7. (Google how to get Discord channels ID's if unknown)

5.) Drop jar into plugins folder on your Spigot Minecraft server, run server, configure config.yml with credentials and restart server. Enjoy!

## Compiling/Setup

1.) change directories cd PATH replace path with your path.

2.) git clone https://github.com/TheLimeGlass/WhitelistDiscord

3.) In the directory execute gradle build if you have gradle installed or ./gradlew build on unix devices, gradlew build works for windows.

4.) Compiled version will be in /build/lib/