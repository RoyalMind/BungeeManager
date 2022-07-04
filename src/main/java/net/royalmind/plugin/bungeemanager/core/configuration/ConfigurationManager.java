package net.royalmind.plugin.bungeemanager.core.configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ListIterator;

import com.google.gson.Gson;

import de.leonhard.storage.Yaml;

import net.royalmind.plugin.bungeemanager.BungeeManager;
import net.royalmind.plugin.bungeemanager.core.Logger;

public class ConfigurationManager {
    private static ConfigurationManager INSTANCE;
    private final BungeeManager PLUGIN;
    private final Gson GSON;

    private ConfigurationManager() {
        INSTANCE = this;
        PLUGIN = BungeeManager.getInstance();
        GSON = PLUGIN.getGson();
    }

    private File loadOrCreateFile(final String filePath, final boolean isInternal) {
        final File file = isInternal ? new File(PLUGIN.getDataFolder(), filePath) : new File(filePath);

        if (file.exists()) Logger.debug("Archivo encontrado.");
        else {
            try {
                Logger.debug("No se encontro el archvo especificdo, Creando archivo "
                        + file.getName()
                        + " con valores por defecto");

                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (Exception ex) {
                Logger.error("No se pudo crear el archivo: " + file.getName(), ex);
            }
        }

        return file;
    }

    private <T> T fillModel(final T model, final Class<?> clazz, final Yaml yamlConfiguration) {
        for (final Field field : clazz.getDeclaredFields()) {
            try {
                final ConfigValue configValue = field.getAnnotation(ConfigValue.class);
                if (configValue != null) {
                    field.setAccessible(true);

                    Logger.debug("Parametro: " + configValue.value());
                    Object objectValue = yamlConfiguration.get(configValue.value());

                    if (objectValue != null) {
                        if (field.getType().getName().equalsIgnoreCase(List.class.getName())) {
                            final ListIterator<Object> listIterator = ((List<Object>) objectValue).listIterator();

                            while (listIterator.hasNext()) {
                                final String next = (String) listIterator.next();

                                if (next instanceof String) listIterator.set((String) next);
                            }
                        }
                        else {
                            String valueAsText = objectValue.toString().trim();

                            switch (field.getType().getName().toLowerCase()) {
                                case "java.lang.boolean":
                                case "boolean":
                                    field.setBoolean(model, Boolean.parseBoolean(valueAsText));
                                    break;
                                case "java.lang.integer":
                                case "integer":
                                case "int":
                                    field.setInt(model, Integer.parseInt(valueAsText));
                                    break;
                                case "java.lang.long":
                                case "long":
                                    field.setLong(model, Long.parseLong(valueAsText));
                                    break;
                                case "java.lang.double":
                                case "double":
                                    field.setDouble(model, Double.parseDouble(valueAsText));
                                    break;
                                case "java.lang.float":
                                case "float":
                                    field.setFloat(model, Float.parseFloat(valueAsText));
                                    break;
                                case "java.lang.short":
                                case "short":
                                    field.setShort(model, Short.parseShort(valueAsText));
                                    break;
                                case "java.lang.byte":
                                case "byte":
                                    field.setByte(model, Byte.parseByte(valueAsText));
                                    break;
                                case "java.lang.string":
                                case "string":
                                default:
                                    field.set(model, valueAsText);
                                    break;
                            }
                        }
                    }
                    else {
                        Logger.debug("Registrando nuevo parametro."
                                + "\nParametro: " + configValue.value()
                                + "\nTipo: " + field.getType().getName()
                                + "\nValor: " + field.get(model).toString());

                        yamlConfiguration.set(configValue.value(), field.get(model));
                    }
                }
            }
            catch (Exception ex) { Logger.error("No se pudo establecer el valor de configuración para el campo '" + field.getName() + "' en " + clazz, ex); }
        }

        return model;
    }

    public <T> T loadYaml(final Class<?> clazz, final String filePath) {
        T instance = null;

        try {
            instance = (T) clazz.getConstructor().newInstance();

            if (instance == null) throw new IllegalArgumentException("El modelo de datos no debe ser nulo.");
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Logger.error("Error al inicializar el modelo de datos \"" + clazz.getName() + "\".", ex);
        }

        final Yaml ymlLoaded = new Yaml(loadOrCreateFile(filePath, !filePath.startsWith("/")));

        instance = fillModel(instance, clazz, ymlLoaded);

        return instance;
    }

    public void loadYamlDefaultConfiguration() { loadYaml(DatabaseSettings.class, "config.yml"); }

    public <T> T loadJson(final String json) {
        T model = null;

        try { model = (T) GSON.fromJson(json, model.getClass()); }
        catch (Exception ex) { ex.printStackTrace(); }

        return model;
    }

    public static ConfigurationManager getInstance() { return INSTANCE != null ? INSTANCE : new ConfigurationManager(); }
}
