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
        Logger.debug("Llenando modelo " + clazz.getSimpleName() + ".");
        for (final Field field : clazz.getDeclaredFields()) {

            final SettingsField settingsField = field.getAnnotation(SettingsField.class);
            if (settingsField != null) {
                try {
                    Logger.debug("Estableciendo accesibilidad del campo.");
                    field.setAccessible(true);

                    Logger.debug("Iniciando lectura de campo: " + settingsField.path());
                    final Object objectValue = yamlConfiguration.get(settingsField.path());

                    if (objectValue != null) {
                        Logger.debug("Campo encontrado, estableciendo valor...");
                        if (field.getType().getName().equalsIgnoreCase(List.class.getName())) {
                            Logger.debug("El campo es una lista, leyendo...");
                            final ListIterator<Object> listIterator = ((List<Object>) objectValue).listIterator();

                            while (listIterator.hasNext()) {
                                final String next = (String) listIterator.next();

                                if (next instanceof String) listIterator.set((String) next);
                            }
                        }
                        else {
                            String valueAsText = objectValue.toString().trim();

                            Logger.debug("Valor leído: " + valueAsText);

                            if (Boolean.class.equals(settingsField.type())) {
                                Logger.debug("El campo se trata como un Boolean Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.setBoolean(model, Boolean.parseBoolean(valueAsText));
                            } else if (Integer.class.equals(settingsField.type()) && int.class.equals(settingsField.type())) {
                                Logger.debug("El campo se trata como un Int/Integer Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.setInt(model, Integer.parseInt(valueAsText));
                            } else if (Long.class.equals(settingsField.type())) {
                                Logger.debug("El campo se trata como un Long Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.setLong(model, Long.parseLong(valueAsText));
                            } else if (Double.class.equals(settingsField.type())) {
                                Logger.debug("El campo se trata como un Long Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.setDouble(model, Double.parseDouble(valueAsText));
                            } else if (Float.class.equals(settingsField.type())) {
                                Logger.debug("El campo se trata como un Float Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.setFloat(model, Float.parseFloat(valueAsText));
                            } else if (Short.class.equals(settingsField.type())) {
                                Logger.debug("El campo se trata como un Short Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.setShort(model, Short.parseShort(valueAsText));
                            } else if (Byte.class.equals(settingsField.type())) {
                                Logger.debug("El campo se trata como un Byte Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.setByte(model, Byte.parseByte(valueAsText));
                            } else {
                                Logger.debug("El campo se trata como un Object Tipo: " + settingsField.type() + " Native:" + objectValue.getClass().getName());
                                field.set(model, objectValue);
                            }

                            Logger.debug("Datos establecidos:"
                                    + "\nParametro: " + settingsField.path()
                                    + "\nTipo esperado: " + settingsField.type().getName()
                                    + "\nTipo: " + objectValue.getClass().getName()
                                    + "\nValor: " + objectValue);
                        }
                    }
                    else {
                        Logger.debug("Campo no encontrado, asignando valor por defecto...");
                        Object fieldValue = field.get(model);
                        Logger.debug("Registrando nuevo parametro."
                                + "\nParametro: " + settingsField.path()
                                + "\nTipo: " + settingsField.type().getName()
                                + "\nValor: " + fieldValue.toString());

                        yamlConfiguration.set(settingsField.path(), fieldValue);
                    }

                } catch (Exception ex) {
                    Logger.error("No se pudo establecer el valor de configuración para el campo '"
                            + field.getName()
                            + "' en "
                            + clazz, ex);
                }
            }
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

    public <T> T loadYamlDefaultConfiguration() { return loadYaml(ConfigurationModel.class, "config.yml"); }

    public <T> T loadJson(final String json) {
        T model = null;

        try { model = (T) GSON.fromJson(json, model.getClass()); }
        catch (Exception ex) { ex.printStackTrace(); }

        return model;
    }

    public static ConfigurationManager getInstance() { return INSTANCE != null ? INSTANCE : new ConfigurationManager(); }
}
