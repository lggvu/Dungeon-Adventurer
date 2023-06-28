package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.soulknight.ability.Ability;
import com.mygdx.soulknight.ability.AbilityDrawer;
import com.mygdx.soulknight.ability.MaxHPIncrease;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Weapon.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CoolDownBar {
    private static final TextureRegion defaultTexture = new TextureRegion(new Texture("hire-designer.png"));
    private SpriteBatch batch = new SpriteBatch();
    private Player player;
    private CompareAbility sorter = new CompareAbility();
    private ArrayList<AbilityDrawer> lstDraw = new ArrayList<>();
    private int numDraw = 7;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final class CompareAbility implements Comparator<AbilityDrawer> {
        @Override
        public int compare(AbilityDrawer o1, AbilityDrawer o2) {
//            o1 < o2 -> -1; o1 > o2 -> 1
            if (o1 instanceof Weapon && o2 instanceof Weapon) {
                if (player.getCurrentWeapon().equals(o1)) {
                    return -1;
                }
                if (player.getCurrentWeapon().equals(o2)) {
                    return 1;
                }
                return 0;
            }
            if (o1 instanceof Weapon) {
                return -1;
            }
            if (o2 instanceof Weapon) {
                return 1;
            }
            if (o1.getTotalTimeCoolDown() == 0 && o2.getCurrentTimeCoolDown() == 0) {
                return o1.getClass().getName().compareTo(o2.getClass().getName());
            }
            if (o1.getTotalTimeCoolDown() == 0) {
                return -1;
            }
            if (o2.getTotalTimeCoolDown() == 0) {
                return 1;
            }
            float o1TimeLeft = o1.getTotalTimeCoolDown() - o1.getCurrentTimeCoolDown();
            float o2TimeLeft = o2.getTotalTimeCoolDown() - o2.getCurrentTimeCoolDown();
            if (o1TimeLeft < o2TimeLeft) {
                return -1;
            } else if (o1TimeLeft > o2TimeLeft) {
                return 1;
            }
            return o1.getClass().getName().compareTo(o2.getClass().getName());
        }
    }
    public CoolDownBar(Player player) {
        this.player = player;
    }

    public void draw() {
        lstDraw.clear();
        for (Weapon weapon : player.getWeapons()) {
            lstDraw.add(weapon);
        }
        for (Ability ability : player.getAbilityArrayList()) {
            lstDraw.add(ability);
        }

        Collections.sort(lstDraw, sorter);

        float heightBoard = 100, margin = 3;
        float gridSize = heightBoard - margin * 2;
        float widthBoard = gridSize * numDraw + margin * (numDraw + 1);
        float startBoard = (Gdx.graphics.getWidth() - widthBoard) / 2;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f,0.5f,0,1);
        shapeRenderer.rect(startBoard, 0, widthBoard, heightBoard);
        shapeRenderer.setColor(0,0,0,1);
        for (int i = 0; i < numDraw; i++) {
            shapeRenderer.rect(startBoard + margin * (i + 1) + gridSize * i,
                    margin, gridSize, gridSize);
        }
        shapeRenderer.end();

        batch.begin();
        for (int i = 0; i < numDraw; i++) {
            if (i < lstDraw.size()) {
                batch.draw(lstDraw.get(i).getTextureCoolDown(), startBoard + margin * (i + 1) + gridSize * i,
                        margin, gridSize, gridSize);
            }
        }
        batch.end();

        // Start the shape rendering
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(229/255f, 229/255f, 229/255f, 0.5f);
        float height;
        AbilityDrawer ad;
        for (int i = 0; i < numDraw; i++) {
            if (i < lstDraw.size()) {
                ad = lstDraw.get(i);
                if (ad.getTotalTimeCoolDown() > 0 && ad.getCurrentTimeCoolDown() < ad.getTotalTimeCoolDown()) {
                    height = ad.getCurrentTimeCoolDown() / ad.getTotalTimeCoolDown() * gridSize;
                    shapeRenderer.rect(startBoard + margin * (i + 1) + gridSize * i,
                            margin, gridSize, height);
                }
            }
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
