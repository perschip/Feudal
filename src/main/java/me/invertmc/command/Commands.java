package me.invertmc.command;

import java.util.ArrayList;
import java.util.List;

import me.invertmc.Feudal;
import me.invertmc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private List<Command> commands = new ArrayList<Command>();
    //!this.hasRank(p, Rank.ALL)

    public Commands(){
        /*commands.add(new HelpCommand());
        commands.add(new TutorialCommand());
        commands.add(new AboutCommand());
        commands.add(new MarketCommand());
        commands.add(new SellCommand());
        commands.add(new PriceCommand());
        commands.add(new UnenchantCommand());

        commands.add(new CharacterCommand());
        commands.add(new ChangeProfessionCommand());

        commands.add(new CreateKingdomCommand());
        commands.add(new ManageCommand());
        commands.add(new JoinCommand());
        commands.add(new InviteCommand());
        commands.add(new ListCommand());
        commands.add(new TreasuryCommand());
        commands.add(new InfoCommand());
        commands.add(new DescriptionCommand());
        commands.add(new NameCommand());

        commands.add(new ChatCommand());
        commands.add(new SparCommand());
        commands.add(new EnemyCommand());
        commands.add(new NeutralCommand());
        commands.add(new AllyCommand());
        commands.add(new SetOpenCommand());
        commands.add(new BanInfoCommand());
        commands.add(new KingdomOnlineCommand());
        commands.add(new OnlineCommand());
        commands.add(new ShieldCommand());

        commands.add(new ChallengeCommand());
        commands.add(new DeleteCommand());
        commands.add(new LeaderCommand());
        commands.add(new LeaveCommand());
        commands.add(new KickCommand());
        commands.add(new ClaimCommand());
        commands.add(new UnclaimCommand());
        commands.add(new SetHomeCommand());
        commands.add(new HomeCommand());
        commands.add(new ViewCommand());
        commands.add(new PromoteCommand());
        commands.add(new DemoteCommand());
        commands.add(new FighterCommand());
        commands.add(new FindClaims());

        commands.add(new RedistributeCommand());

        commands.add(new SetProfessionLevelCommand());
        commands.add(new SetSpeedCommand());
        commands.add(new SetStrengthCommand());
        commands.add(new SetToughnessCommand());
        commands.add(new SetStaminaCommand());
        commands.add(new SetLuckCommand());
        commands.add(new ResetCharacterCommand());
        commands.add(new SetReputationCommand());
        commands.add(new ForceWinCommand());
        commands.add(new ReloadCommand());
        commands.add(new KingdomLogCommand());

        //SERVER COMMANDS
        commands.add(new ServerChangeReputationCommand());
        commands.add(new ServerReloadCommand());
        commands.add(new ServerEditXPLevelCommand());*/
    }

    public List<Command> getCommands(){
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             org.bukkit.command.Command cmd, String label, String[] args) {
        process(sender, args);
        return true;
    }

    public void process(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;

            boolean found = false;
            for(Command command : commands){
                if(args.length == 0){
                    found = true;
                    CommandHelp.help(1, p);
                    break;
                }else if(command.getType().equals(Command.CommandType.PLAYER)){
                    if(command.run(args, p)){
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                p.sendMessage(Feudal.getMessage("commands.3216"));
            }
        }else{
            boolean found = false;
            for(Command command : commands){
                if(args.length == 0){
                    Bukkit.getConsoleSender().sendMessage(Feudal.getMessage("commands.3264"));
                }else if(command.getType().equals(Command.CommandType.SERVER)){
                    if(command.run(args, null)){
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                Bukkit.getConsoleSender().sendMessage(Feudal.getMessage("commands.3264"));
            }
        }
    }

    public boolean preprocess(String message, Player player) {
        if(Utils.isFeudalCommand(message)) {
            String[] args = message.split(" ");
            String[] args2 = new String[args.length-1];
            for(int i = 1; i < args.length; i++){
                args2[i-1] = args[i];
            }
            process(player, args2);
            return true;
        }
        return false;
    }
}
