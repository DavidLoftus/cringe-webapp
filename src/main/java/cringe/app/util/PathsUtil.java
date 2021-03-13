package cringe.app.util;

import cringe.app.db.Artifact;
import cringe.app.db.Game;

public class PathsUtil {
    public String play(Game game) {
        return String.format("/game/%d/play", game.getId());
    }

    public String buy(Game game) {
        return String.format("/game/%d/buy", game.getId());
    }

    public String view(Game game) {
        return String.format("/game/%d", game.getId());
    }

    public String edit(Game game) {
        return String.format("/admin/game/%d", game.getId());
    }

    public String upload(Game game) {
        return String.format("/admin/game/%d/upload", game.getId());
    }

    public String get(Artifact artifact) {
        if (artifact == null) {
            return "/artifact/-1/missing_artifact";
        }
        return String.format("/artifact/%d/%s", artifact.getId(), artifact.getFileName());
    }
}
