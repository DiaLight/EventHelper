package dialight.modulelib;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMapWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public class ModuleLib extends Project {

    private final ObservableMapWrapper<String, Module> toolregistry = new ObservableMapWrapper<>();
    private final ObservableCollection<Module> immutableObservable = toolregistry.asImmutableCollectionObaervable(Module::getId);


    public ModuleLib(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {

    }

    @Override
    public void disable() {

    }

    @Override
    public ProjectApi getApi() {
        return new ModuleLibApi(this);
    }

    public ObservableCollection<Module> getModules() {
        return immutableObservable;
    }

    public void register(Module module) {
        toolregistry.put(module.getId(), module);
    }

}
