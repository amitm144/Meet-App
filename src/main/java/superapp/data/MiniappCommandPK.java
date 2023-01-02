package superapp.data;

import java.io.Serializable;
import java.util.Objects;

public class MiniappCommandPK implements Serializable {
    /* This class creates composite primary key for Miniapp command */
    private String superapp;
    private String miniapp;
    private String internalCommandId;

    public MiniappCommandPK() {}

    public MiniappCommandPK(String superapp, String miniapp, String internalCommandId) {
        this.superapp = superapp;
        this.miniapp = miniapp;
        this.internalCommandId = internalCommandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniappCommandPK that = (MiniappCommandPK) o;
        return  Objects.equals(superapp, that.superapp) &&
                Objects.equals(miniapp, that.miniapp) &&
                Objects.equals(internalCommandId, that.internalCommandId);
    }

    @Override
    public int hashCode() { return Objects.hash(superapp, miniapp, internalCommandId); }
}
