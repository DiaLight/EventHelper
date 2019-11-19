package dialight.modulelib;

import dialight.eventhelper.project.ProjectApi;
import dialight.observable.collection.ObservableCollection;

public class ModuleLibApi implements ProjectApi {

    private final ModuleLib proj;

    public ModuleLibApi(ModuleLib proj) {
        this.proj = proj;
    }

    public ObservableCollection<Module> getModules() {
        return proj.getModules();
    }


    public void register(Module module) {
        this.proj.register(module);
    }

}
