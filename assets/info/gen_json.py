import json

dct_weapon = {
    "Gun 1": {
        "type": "Gun",
        "gun_texture": "weapon/weapon.png",
        "bullet_texture": "bullet/bullet1.png", 
        "properties": {
            "damage":3,
            "energy_cost":1,
            # "bullets_per_time":1,
            "attack_speed":1.2,
            "range":400,
            "critical_rate":0.25,
            "bullet_speed":600.0
        }
    },
    "Gun 2": {
        "type": "Gun",
        "gun_texture": "weapon/weapon1.png",
        "bullet_texture": "bullet/bullet3.png",
        "properties": {
            "damage":5,
            "energy_cost":2,
            # "bullets_per_time":1,
            "attack_speed":0.9,
            "range":600,
            "critical_rate":0.4,
            "bullet_speed":1000.0
        }
    },
    "Gun Monster 1": {
        "type": "Gun",
        "gun_texture": "weapon/weapon.png",
        "bullet_texture": "bullet/bullet1.png",
        "properties": {
            "damage":3,
            "energy_cost":2,
            # "bullets_per_time":1,
            "attack_speed":2.0,
            "range":600,
            "critical_rate":0.4,
            "bullet_speed":90.0
        }
    },
    "Sword 1": {
        "type": "Sword",
        "sword_texture": "weapon/sword.png",
        "effect_texture": {
            'path': "weapon/sword-effect.png",
            "paddingWidth": 16,
            "paddingHeight": 30,
            "gapWidth": 24,
            "gapHeight": 50,
            "imgWidth": 100,
            "imgHeight": 100,
            "startCol": 0,
            "startRow": 1,
            "frameCols": 2,
            "frameRows": 1,
        },
        "properties": {
            "damage": 8,
            "energy_cost":2,
            "attack_speed":0.8,
            "range":100,
            "critical_rate":0.7,
        }
    }
}

json_object = json.dumps(dct_weapon, indent = 4) 
with open('weapon_info.json', 'w') as f:
    f.write(json_object)


dct_character = {
    'king': {
        'type': 'hero',
        'texture_path': 'character/img1.png',
        'hp': 7,
        'armor': 6,
        # 'time_heal_armor': 1.5,
        'energy': 200,
        'speed_run': 180,
        'default_weapon': 'Sword 1'
    },

    'knight': {
        'type': 'monster',
        'texture_path': 'character/img1.png',
        'hp': 7,
        'speed_run': 90,
        'default_weapon': 'Gun Monster 1'
    }
}

json_object = json.dumps(dct_character, indent = 4) 
with open('character_info.json', 'w') as f:
    f.write(json_object)
