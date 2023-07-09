package com.mygdx.soulknight.entity.Character.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.game.Settings;
import com.mygdx.soulknight.entity.Skill;

public class Assassin extends Player {
    private float timeFromThrow = 0;
    private Texture kunaiTexture = new Texture("bullet/kunai.png");
    private Kunai kunai;
    public Assassin() {
        super("assassin", null);
    }
    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        kunai = new Kunai();
        kunai.setOwner(this);
        specialSkill = new Skill(new TextureRegion(new Texture(textureSpecPath)), 1f, 2f) {
            @Override
            public void activateSkill() {
                super.activateSkill();
                timeFromThrow = 0;
                kunai.attack(getLastMoveDirection());
            }
            @Override
            public void deactivateSkill(){
                super.deactivateSkill();
                kunai.isFlying=false;
            }
        };
        return source;
    }

    public class Kunai {
        private float x, y, degree;
        private Vector2 direction;

        protected SimpleCharacter owner;
        private TextureRegion kunaiTexture = new TextureRegion(new Texture("weapon/kunai.png"));
        private float speed = 300f;
        public boolean isFlying=false;
        public Kunai(){}
        public void setOwner(SimpleCharacter owner) {
            this.owner = owner;
        }
        public void attack(Vector2 direction){
            this.x = this.owner.getX();
            this.y = this.owner.getY();
            this.direction = direction;
            this.isFlying = true;
        }
        public void update(float deltaTime, WorldMap map){
            if(this.isFlying) {

                float testX = this.x + direction.x * speed * deltaTime;
                float testY = this.y + direction.y * speed * deltaTime;
                Vector2 temp = new Vector2(0, 0);
                Rectangle rectangleTest = new Rectangle(this.x, testY, owner.getMaxWidth(), owner.getMaxHeight());
                if (!map.isMapCollision(rectangleTest)){
                    this.y = testY;
                    temp = temp.add(0, direction.y);
                }
                rectangleTest = new Rectangle(testX, this.y, owner.getMaxWidth(), owner.getMaxHeight());
                if (!map.isMapCollision(rectangleTest)) {
                    this.x = testX;
                    temp = temp.add(direction.x, 0);
                }
                direction = temp;
            }
        }
        public void draw(SpriteBatch batch){
            float height = 10, width = kunaiTexture.getTexture().getWidth() * height / kunaiTexture.getTexture().getHeight();
            if (direction.x != 0 || direction.y != 0) {
                degree = direction.angleDeg(new Vector2(1, 0));
            }
            batch.draw(kunaiTexture, this.x,this.y, width/2,height/2, width, height,1,1 , degree);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (specialSkill.isInProgresss()) {
            kunai.update(deltaTime,getMap());
            if (Gdx.input.isKeyPressed(Settings.getKeyCode(Settings.GameButton.SPECIAL_SKILL)) && timeFromThrow > 0.3f) {
                setX(kunai.x);
                setY(kunai.y);
                specialSkill.stopImplement();
            }
            timeFromThrow += deltaTime;
        }
    }
    @Override
    public void draw(SpriteBatch batch) {

        super.draw(batch);
        if (specialSkill.isInProgresss()) {
            kunai.draw(batch);
        }
    }
}


