package pl.pollub.hirols.gui.town;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisTextButton;
import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.managers.HudManager;
import pl.pollub.hirols.managers.UnitsManager;

/**
 * Created by erykp_000 on 2017-01-04.
 */
public class Tavern extends TownBuilding {
    private Hirols game;

    public Tavern(Hirols game) {
        super("Tawerna");
        this.game = game;

        UnitsManager.Hero heroWarior = game.unitsManager.heroOrcWarrior;
        HeroRecruitTable heroRecruitTable = new HeroRecruitTable(heroWarior);
        add(heroRecruitTable).row();
        UnitsManager.Hero heroMage = game.unitsManager.heroOrcMage;
         heroRecruitTable = new HeroRecruitTable(heroMage);
        add(heroRecruitTable);
    }

    @Override
    public boolean upgrade() {
        return false;
    }

    private class HeroRecruitTable extends Table {
        public HeroRecruitTable(UnitsManager.Hero hero) {
            Image image = new Image(game.assetManager.get(hero.avatarPath, Texture.class));
            image.setScaling(Scaling.fit);
            add(image).width(100).pad(10);

            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, HudManager.FONT_COLOR);
            Label heroName = new Label(hero.name , labelStyle);
            add(heroName).pad(20);

            Label cost = new Label("2000 Gold", labelStyle);
            add(cost).pad(20);

            VisTextButton.VisTextButtonStyle textButtonStyle = new VisTextButton.VisTextButtonStyle(game.hudManager.skin.get("text-button", VisTextButton.VisTextButtonStyle.class));
            TextButton recruitButton = new TextButton("Recruit hero", textButtonStyle);
            add(recruitButton).pad(20).fill();
        }
    }
}
