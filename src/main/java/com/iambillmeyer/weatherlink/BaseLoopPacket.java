package com.iambillmeyer.weatherlink;

public abstract class BaseLoopPacket
{
    public enum BarTrend
    {
        FALLING_RAPIDLY(196), FALLING_SLOWLY(236), STEADY(0), RISING_SLOWLY(20), RISING_RAPIDLY(60);

        private int ordinal;

        private BarTrend(int ordinal)
        {
            this.ordinal = ordinal;
        }

        public static BarTrend fromOrdinal(int ordinal)
        {
            switch (ordinal)
            {
                case -60:
                    return FALLING_RAPIDLY;
                case -20:
                    return FALLING_SLOWLY;
                case 0:
                    return STEADY;
                case 20:
                    return RISING_SLOWLY;
                case 60:
                    return RISING_RAPIDLY;
            }

            return STEADY;
        }
    }

    public abstract void parse(byte[] buf);
}
