package me.manaki.plugin.quests.category;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum CooldownType {

    DAILY {
        @Override
        public boolean enough(LocalDate d1, LocalDate d2) {
            return Math.abs(ChronoUnit.DAYS.between(d1, d2)) >= 1;
        }
    },
    WEEKLY{
        @Override
        public boolean enough(LocalDate d1, LocalDate d2) {
            return Math.abs(ChronoUnit.DAYS.between(d1, d2)) >= 7;
        }
    },
    MONTHLY{
        @Override
        public boolean enough(LocalDate d1, LocalDate d2) {
            return Math.abs(ChronoUnit.MONTHS.between(d1, d2)) >= 1;
        }
    };

    CooldownType () {}

    public abstract boolean enough(LocalDate d1, LocalDate d2);

}
