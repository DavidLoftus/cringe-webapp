package cringe.app.analytics;

import cringe.app.db.Game;

public class GameSale {
    private Game game;
    private float totalSales;

    public GameSale(Game game, float totalSales) {
        this.game = game;
        this.totalSales = totalSales;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public float getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(float totalSales) {
        this.totalSales = totalSales;
    }
}
