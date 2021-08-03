package me.manaki.plugin.quests.quest.requirement;

import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.utils.data.DataValue;
import me.manaki.plugin.quests.utils.data.ValueType;
import org.bukkit.entity.Player;

import java.time.LocalDate;

public enum RequirementType {

    QUEST_DONE {
        @Override
        public DataValue getDefault() {
            return new DataValue(ValueType.STRING, "q1");
        }

        @Override
        public boolean check(Player player, String v) {
            var quester = Questers.get(player.getName());
            if (quester == null) return false;
            return new DataValue(this.getDefault().getType(), v).compare(quester.getCompletedQuests().keySet());
        }
    },
    DATE {
        @Override
        public DataValue getDefault() {
            // none = equals
            // > = later
            // < = before
            return new DataValue(ValueType.STRING, "> 01/01/2001");
        }

        @Override
        public boolean check(Player player, String v) {
            var s = v;
            var c = s.contains(" ") ? s.split(" ")[0] : null;

            v = s.contains(" ") ? s.split(" ")[1] : s;

            int day = Integer.parseInt(v.split("/")[0]);
            int month = Integer.parseInt(v.split("/")[1]);
            int year = Integer.parseInt(v.split("/")[2]);
            var date = LocalDate.of(year, month, day);
            var now = LocalDate.now();

            if (c == null) {
                return date.isEqual(now);
            }

            switch (c) {
                case ">":
                    return now.isAfter(date);
                case "<":
                    return now.isBefore(date);
                case "=":
                    return now.isEqual(date);
                case ">=":
                    return now.isEqual(date) || now.isAfter(date);
                case "<=":
                    return now.isEqual(date) || now.isBefore(date);
            }

            return false;
        }
    },
    LEVEL {
        @Override
        public DataValue getDefault() {
            return new DataValue(ValueType.NUMBER, "> 10");
        }

        @Override
        public boolean check(Player player, String v) {
            return new DataValue(this.getDefault().getType(), v).compare(player.getLevel());
        }
    },
    NOT_HAVE_PERM {
        @Override
        public DataValue getDefault() {
            return new DataValue(ValueType.STRING, "member.20");
        }

        @Override
        public boolean check(Player player, String v) {
            return !player.hasPermission(v);
        }
    }
    
    ;
    
    public abstract DataValue getDefault();

    public abstract boolean check(Player player, String v);

}
