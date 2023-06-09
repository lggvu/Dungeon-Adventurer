import json

dct_weapon = {
    "Gun 1": {
        "type": "Gun",
        "gun_texture": "weapon/weapon.png",
        "bullet_texture": "bullet/bullet1.png",
        "properties": {
            "damage": 3,
            "energy_cost": 1,
            "attack_speed": 1.2,
            "range": 400,
            "critical_rate": 0.25,
            "bullet_speed": 600.0
        }
    },
    "Gun 2": {
        "type": "Gun",
        "gun_texture": "weapon/weapon1.png",
        "bullet_texture": "bullet/bullet3.png",
        "properties": {
            "damage": 5,
            "energy_cost": 2,
            "attack_speed": 0.9,
            "range": 600,
            "critical_rate": 0.4,
            "bullet_speed": 1000.0
        }
    },
    "Gun Monster 1": {
        "type": "Gun",
        "gun_texture": "weapon/weapon.png",
        "bullet_texture": "bullet/bullet1.png",
        "properties": {
            "damage": 3,
            "energy_cost": 2,
            "attack_speed": 2.0,
            "range": 600,
            "critical_rate": 0.4,
            "bullet_speed": 90.0
        }
    },
    "Sword 1": {
        "type": "Sword",
        "sword_texture": "weapon/sword.png",
        "effect_texture": {
            "path": "weapon/sword-effect.png",
            "paddingWidth": 16,
            "paddingHeight": 30,
            "gapWidth": 24,
            "gapHeight": 50,
            "imgWidth": 100,
            "imgHeight": 100,
            "startCol": 0,
            "startRow": 1,
            "frameCols": 2,
            "frameRows": 1
        },
        "properties": {
            "damage": 8,
            "energy_cost": 2,
            "attack_speed": 0.8,
            "range": 100,
            "critical_rate": 0.7
        }
    }
}

json_object = json.dumps(dct_weapon, indent = 4) 
with open('weapon_info.json', 'w') as f:
    f.write(json_object)


dct_character = {
    "king": {
        "type": "hero",
        "texture_path": "character/img1.png",
        "hp": 7,
        "armor": 6,
        "energy": 200,
        "speed_run": 180,
        "default_weapon": "Sword 1"
    },
    "adventurer": {
        "type": "hero",
        "texture_path": "character/adventurer.png",
        "hp": 7,
        "armor": 6,
        "energy": 200,
        "speed_run": 180,
        "default_weapon": "Sword 1"
    },
    "knight": {
        "type": "monster",
        "texture_path": "character/img1.png",
        "hp": 7,
        "speed_run": 90,
        "default_weapon": "Gun Monster 1"
    },
    "teacher": {
        "type": "boss",
        "texture_path": "character/img1.png",
        "hp": 20,
        "speed_run": 30,
	"default_weapon": "Gun Monster 1"
    }
}
json_object = json.dumps(dct_character, indent = 4) 
with open('character_info.json', 'w') as f:
    f.write(json_object)


dct_potion = {
    "HP Stone": {
        "type": "Life Potion",
        "texture_path": "item/hp_stone.png",
        "width": 10,
        "height": 10,
        "auto_collect": True,
        "hp": 1
    },
    "Life Potion": {
        "type": "Life Potion",
        "texture_path": "item/life_potion.png",
        "width": 20,
        "height": 20,
        "auto_collect": False,
        "hp": 2
    },
    "Mana Stone": {
        "type": "Mana Potion",
        "texture_path": "item/mana_stone.png",
        "width": 10,
        "height": 10,
        "auto_collect": True,
        "mana": 20
    },
    "Mana Potion": {
        "type": "Mana Potion",
        "texture_path": "item/mana_potion.png",
        "width": 20,
        "height": 20,
        "auto_collect": False,
        "mana": 40
    }
}
json_object = json.dumps(dct_potion, indent = 4) 
with open('item_info.json', 'w') as f:
    f.write(json_object)

dct_sprite = {
    "character/img1.png": {
        "paddingWidth": 0,
        "paddingHeight": 0,
        "gapWidth": 0,
        "gapHeight": 0,
        "imgCharacterWidth": 48,
        "imgCharacterHeight": 72,
        "totalCols": 12,
        "totalRows": 8,
        "character": {
            "king": {
                "startCol": 0,
                "startRow": 0,
                "frameCols": 3,
                "frameRows": 4,
                "direction": {
                    "270": 0,
                    "180": 1,
                    "0": 2,
                    "90": 3
                }
            },
            "princess": {
                "startCol": 6,
                "startRow": 0,
                "frameCols": 3,
                "frameRows": 4,
                "direction": {
                    "270": 0,
                    "180": 1,
                    "0": 2,
                    "90": 3
                }
            },
            "lady": {
                "startCol": 0,
                "startRow": 4,
                "frameCols": 3,
                "frameRows": 4,
                "direction": {
                    "270": 0,
                    "180": 1,
                    "0": 2,
                    "90": 3
                }
            },
            "teacher": {
                "startCol": 3,
                "startRow": 4,
                "frameCols": 3,
                "frameRows": 4,
                "direction": {
                    "270": 0,
                    "180": 1,
                    "0": 2,
                    "90": 3
                }
            },
            "knight": {
                "startCol": 3,
                "startRow": 0,
                "frameCols": 3,
                "frameRows": 4,
                "direction": {
                    "270": 0,
                    "180": 1,
                    "0": 2,
                    "90": 3
                }
            }
            
        }
    },
    "character/img2.png": {
        "paddingWidth": 0,
        "paddingHeight": 0,
        "gapWidth": 25,
        "gapHeight": 28,
        "imgCharacterWidth": 50,
        "imgCharacterHeight": 90,
        "totalCols": 8,
        "totalRows": 8,
        "character": {
            "young": {
                "startCol": 0,
                "startRow": 0,
                "frameCols": 8,
                "frameRows": 8,
                "direction": {
                    "270": 0,
                    "180": 1,
                    "0": 2,
                    "90": 3,
                    "225": 4,
                    "315": 5,
                    "135": 6,
                    "45": 7
                }
            }
        }
    },
    "character/adventurer.png": {
        "paddingWidth": 0,
        "paddingHeight": 0,
        "gapWidth": 10,
        "gapHeight": 0,
        "imgCharacterWidth": 24,
        "imgCharacterHeight": 34,
        "totalCols": 12,
        "totalRows": 8,
        "character": {
            "adventurer": {
                "startCol": 0,
                "startRow": 0,
                "frameCols": 7,
                "frameRows": 2,
                "direction": {
                    "180": 1,
                    "0": 0
                }
            }
        }
    }
}

json_object = json.dumps(dct_sprite, indent = 4) 
with open('character_sprite_sheet_info.json', 'w') as f:
    f.write(json_object)