package net.royalmind.plugin.bungeemanager.core;

import java.util.logging.Level;
import net.md_5.bungee.api.ProxyServer;
import net.royalmind.library.colorizer.Colorizer;
import net.royalmind.plugin.bungeemanager.BungeeManager;

public class Logger {
    private static java.util.logging.Logger logger;

    public static void raw(final Level level, final String texto, final Object... array)
    {
        final String[] split = String.format(texto, array).split("\n");

        for (int length = split.length, i = 0; i < length; ++i)
        {
            Logger.logger.log(level, split[i]);
        }
    }

    /*
     * Registra un mensaje de información en el log.
     * @param texto Mensaje informativo.
     */
    public static void info(final String texto, final Object... array)
    {
        final String[] split = String.format(texto, array).split("\n");

        for (int length = split.length, i = 0; i < length; ++i)
        {
            Logger.logger.info(Colorizer.decorate("&r[BM]:&r " + split[i]));
        }
    }

    /*
     * Registra un mensaje de advertencia en el log.
     * @param texto Mensaje informativo.
     */
    public static void warn(final String texto, final Object... array)
    {
        final String[] split = String.format(texto, array).split("\n");

        for (int length = split.length, i = 0; i < length; ++i)
        {
            Logger.logger.warning(Colorizer.decorate("&r[BM]:&r " + split[i]));
        }
    }

    /*
     * Registra un mensaje de error grave en el log.
     * @param texto Mensaje informativo.
     */
    public static void severe(final String texto, final Object... array)
    {
        final String[] split = String.format(texto, array).split("\n");

        for (int length = split.length, i = 0; i < length; ++i) {
            Logger.logger.severe(Colorizer.decorate("&r[BM]:&r " + split[i]));
        }
    }

    /*
     * Registra un mensaje de error en el log.
     * @param texto Mensaje informativo.
     */
    public static void error(final String texto, final Object... array)
    {
        final String[] split = String.format(texto, array).split("\n");

        for (int length = split.length, i = 0; i < length; ++i)
        {
            info("&r[BM] &c[ERROR]: &r%s", split[i]);
        }
    }
    /*
     * Registra un mensaje de error en el log.
     * @param texto Mensaje informativo.
     */
    public static void error(final String texto, final Exception exception)
    {
        logger.log(Level.SEVERE, Colorizer.translate("&r[BM] &c[ERROR]: &r")
                + texto
                + Colorizer.translate("\n\t&cDetalle: &r")
                + exception);
    }

    /*
     * Registra un mensaje de depuración en el log.
     * @param texto Mensaje informativo.
     */
    public static void debug(final String texto, final Object... array)
    {
        if (BungeeManager.debug()) {
            final String[] split = String.format(texto, array).split("\n");

            for (int length = split.length, i = 0; i < length; ++i)
            {
                Logger.logger.info(Colorizer.translate("&r[BM]:&r ") + split[i]);
            }
        }
    }

    /*
     * Registra un mensaje de depuración en el log.
     * @param texto Mensaje informativo.
     */
    public static void debugError(final String texto, final Object... array)
    {
        if (BungeeManager.debug()) { error(String.format(texto, array), new Object[0]); }
    }

    public static String getLine() { return Colorizer.translate("&a=&e-&a=&e-&a=&e-&a=&e-&a=&e-&a=&e-&a=&e-&a=&e-&a=&e-&a=&e-&a=&e-&a="); }

    static {
        Logger.logger = ProxyServer.getInstance().getLogger();
    }
}
