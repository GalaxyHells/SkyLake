package com.galaxyhells.skylake.data;

import java.util.HashMap;
import java.util.Map;

public class PriceData {
    
    // Estrutura para preços de itens
    public static class ItemPrice {
        public final String name;
        public final String price;
        public final String description;
        
        public ItemPrice(String name, String price, String description) {
            this.name = name;
            this.price = price;
            this.description = description;
        }
    }
    
    // Estrutura para sets de armadura
    public static class ArmorSet {
        public final String name;
        public final String fullSetPrice;
        public final String scalePrice;
        public final String scaleAmount;
        public final String helmetPrice;
        public final String chestplatePrice;
        public final String leggingsPrice;
        public final String bootsPrice;
        
        public ArmorSet(String name, String fullSetPrice, String scalePrice, String scaleAmount) {
            this.name = name;
            this.fullSetPrice = fullSetPrice;
            this.scalePrice = scalePrice;
            this.scaleAmount = scaleAmount;
            
            // Calcular preços individuais baseados nas escamas
            double scaleValue = parseScaleValue(scalePrice);
            this.helmetPrice = formatPrice(scaleValue * 5); // Capacete = 50 escamas
            this.chestplatePrice = formatPrice(scaleValue * 8); // Peitoral = 80 escamas
            this.leggingsPrice = formatPrice(scaleValue * 7); // Calça = 70 escamas
            this.bootsPrice = formatPrice(scaleValue * 4); // Botas = 40 escamas
        }
        
        private double parseScaleValue(String scalePrice) {
            try {
                String numeric = scalePrice.replace("~", "").replace("k", "").replace("kk", "");
                if (scalePrice.contains("kk")) {
                    return Double.parseDouble(numeric) * 1000000;
                } else if (scalePrice.contains("k")) {
                    return Double.parseDouble(numeric) * 1000;
                } else {
                    return Double.parseDouble(numeric);
                }
            } catch (Exception e) {
                return 0;
            }
        }
        
        private String formatPrice(double value) {
            if (value >= 1000000) {
                return "~" + String.format("%.2f", value / 1000000) + "kk";
            } else if (value >= 1000) {
                return "~" + String.format("%.0f", value / 1000) + "k";
            } else {
                return "~" + String.format("%.0f", value);
            }
        }
    }
    
    // ITENS DRAGÃO
    public static class DragonItems {
        public static final ItemPrice[] DROPS = {
            new ItemPrice("Pedra do Dragão", "~6kk", "Drop do Ender Dragon"),
            new ItemPrice("AOTD", "~30kk", "Aspect of the Dragons"),
            new ItemPrice("Garra do Dragão", "~50kk", "Dragon Claw"),
            new ItemPrice("Pet Dragão", "???", "Pet de Dragão"),
            new ItemPrice("Olho de Invocação", "2kk", "Olho de Invocação (ZOI)")
        };
        
        public static final ArmorSet[] ARMOR_SETS = {
                new ArmorSet("Protetor", "~20kk", "~800k", "10 escamas"),
                new ArmorSet("Ancião", "~12kk", "~500k", "10 escamas"),
                new ArmorSet("Sábio", "~28kk", "~1.150kk", "10 escamas"),
                new ArmorSet("Jovem", "~36kk", "~1.5kk", "10 escamas"),
                new ArmorSet("Instável", "~48kk", "~2kk", "10 escamas"),
                new ArmorSet("Forte", "~60kk", "~2.5kk", "10 escamas"),
                new ArmorSet("Superior", "~240kk", "~10.0kk", "10 escamas")
        };
    }
    
    // HERBALISMO
    public static class Herbalism {
        public static final ItemPrice[] SHOP_ITEMS = {
            new ItemPrice("Livro Hiper Nv 5", "~750k", "Batata, Cenoura, etc"),
            new ItemPrice("Enxada Polinizada", "~1.8kk", "Enxada especial"),
            new ItemPrice("Livro Hidromel 5", "~7.5kk", "Livro de encantamento"),
            new ItemPrice("Salada de Frutas", "~600k", "Item de farming"),
            new ItemPrice("Buquê de Flores", "~850k", "Item decorativo"),
            new ItemPrice("Cristal Iluminado", "~1kk", "Cristal mágico"),
            new ItemPrice("Colar de Girassol", "~1.2kk", "Acessório"),
            new ItemPrice("Dente de Leão", "~2kk", "Item raro"),
            new ItemPrice("Recombinador", "~18.75kk", "Para recombinações")
        };
        
        public static final ItemPrice[] DROPS = {
            new ItemPrice("Adubo", "~400k pack / ~6.25k un", "Fertilizante"),
            new ItemPrice("Raiz", "~38.4kk pack / ~600k un", "Raiz especial")
        };
        
        public static final ItemPrice[] ARMOR_SETS = {
            new ItemPrice("Set Abóbora", "~200k", "Armadura básica"),
            new ItemPrice("Set Broto", "~1kk", "Armadura intermediária"),
            new ItemPrice("Set Raiz", "~8.2kk", "Armadura avançada"),
            new ItemPrice("Set Sol", "~51.4kk", "Armadura superior")
        };

        public static final ItemPrice[] ITEMS = {
            new ItemPrice("Enxada Polinizada", "~1.8kk", "Enxada especial"),
                new ItemPrice("Enxada Devastadora", "???", "Enxada Épica"),
                new ItemPrice("Enxada de Cogumelo", "???", "Enxada Épica"),
                new ItemPrice("Enxada Solar", "???", "Enxada Lendária")
        };
    }
    
    // ITENS PARA MINIONS
    public static class MinionItems {
        public static final ItemPrice[] ITEMS = {
            new ItemPrice("Balde de Lava", "~500k", "Para minions"),
            new ItemPrice("Balde de Magma", "~1.5kk", "Balde encantado"),
            new ItemPrice("Compactador", "~10k", "Compacta itens"),
            new ItemPrice("Super Compactador", "~1kk", "Versão melhorada"),
            new ItemPrice("Depósito Pequeno", "~500 coins", "Armazenamento básico"),
            new ItemPrice("Depósito Médio", "~10k", "Armazenamento intermediário"),
            new ItemPrice("Depósito Grande", "~300k", "Armazenamento avançado"),
            new ItemPrice("Catalisador", "~100k", "Para upgrades"),
            new ItemPrice("Fornalha de Fundição", "~500 coins", "Fornalha especial")
        };
    }
    
    // ARCOS
    public static class Bows {
        public static final ItemPrice[] ITEMS = {
            new ItemPrice("Apollo", "~1kk", "Arco lendário"),
            new ItemPrice("Furação", "~600k", "Arco perfurante")
        };
    }
    
    // ARMAS
    public static class Weapons {
        public static final ItemPrice[] ITEMS = {
            new ItemPrice("Pigman", "~22kk", "Espada do Pigman"),
            new ItemPrice("Aspecto do Fim", "~4kk", "Aspect of the End"),
            new ItemPrice("Espada do Golem", "~800k", "Espada de ferro"),
            new ItemPrice("Espada Saltitante", "~10kk", "Espada com salto")
        };
    }
    
    // LIVROS
    public static class Books {
        public static final ItemPrice[] ITEMS = {
            new ItemPrice("Livro Batata Quente", "200k", "Livro de encantamento"),
            new ItemPrice("Livro Cenoura Quente", "450k", "Livro de encantamento"),
            new ItemPrice("Livro Coelho Assado", "500k", "Livro de encantamento"),
            new ItemPrice("Livro Pútrifero", "200k", "Livro de encantamento")
        };
    }

    // CAÇADOR
    public static class Slayers {
        public static final ItemPrice[] ITEMS = {
                new ItemPrice("Coágulo Sanguineo (64x)", "150k", "Coágulo Sanguineo"),
                new ItemPrice("Medula necrosada", "400k", "Medula necrosada"),
                new ItemPrice("Catalizador de Espectros", "2kk", "Catalizador de Espectros"),
        };
    }
    
    // Mapeamento geral para fácil acesso
    public static class Categories {
        public static final String DRAGON_ITEMS = "Itens Dragão";
        public static final String HERBALISM = "Herbalismo";
        public static final String MINION_ITEMS = "Itens para Minions";
        public static final String BOWS = "Arcos";
        public static final String WEAPONS = "Armas";
        
        public static final Map<String, Object> ALL_CATEGORIES = new HashMap<>();
        
        static {
            ALL_CATEGORIES.put(DRAGON_ITEMS, DragonItems.class);
            ALL_CATEGORIES.put(HERBALISM, Herbalism.class);
            ALL_CATEGORIES.put(MINION_ITEMS, MinionItems.class);
            ALL_CATEGORIES.put(BOWS, Bows.class);
            ALL_CATEGORIES.put(WEAPONS, Weapons.class);
        }
    }
}
