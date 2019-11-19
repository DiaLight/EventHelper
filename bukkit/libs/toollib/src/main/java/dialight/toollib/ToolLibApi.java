package dialight.toollib;

import dialight.eventhelper.project.ProjectApi;
import dialight.observable.collection.ObservableCollection;
import org.jetbrains.annotations.Nullable;

public class ToolLibApi implements ProjectApi {

    private final ToolLib proj;

    public ToolLibApi(ToolLib proj) {
        this.proj = proj;
    }

    public ObservableCollection<Tool> getTools() {
        return proj.getImmutableObservable();
    }

    public void register(Tool tool) {
        this.proj.register(tool);
    }

    @Nullable public <T extends Tool> T get(Class<T> clazz) {
        return this.proj.getTool(clazz);
    }

}
