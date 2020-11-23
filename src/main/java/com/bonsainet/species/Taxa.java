package com.bonsainet.species;

import com.bonsainet.species.model.Taxon;

import java.util.ArrayList;
import java.util.Objects;


public class Taxa {
    public ArrayList<Taxon> taxonArray;

    public Taxa() {
        this.taxonArray = new ArrayList<Taxon>();

        Taxon[] speciesArrayTmp;
        speciesArrayTmp = new Taxon[]{
                new Taxon(  1, "Sapindaceae", "Acer", "palmatumx", "", "Japanese maple", "Deciduous", 0),
                new Taxon( 2, "Fagaceae", "Quercus", "robur", "", "English oak", "Deciduous", 1),
                new Taxon( 3, "Fagaceae", "Fagus", "sylvatica", "", "Beech", "Deciduous", 0),
                new Taxon( 4, "Fagaceae", "Fagus", "sylvatica f. purpurea", "", "Copper beech", "Deciduous", 1),
                new Taxon( 121, "Pinaceae", "Abies", "koreana", "", "Korean fir", "Coniferous", 0),
                new Taxon( 122, "Sapindaceae", "Acer", "buergerianum", "", "Trident Maple", "Deciduous", 0),
                new Taxon( 123, "Sapindaceae", "Acer", "campestre", "", "Field Maple, Hedge Maple", "Deciduous", 0),
                new Taxon( 124, "Sapindaceae", "Acer", "circinatum", "", "Vine Maple", "Deciduous", 0),
                new Taxon( 125, "Sapindaceae", "Acer", "ginnala", "", "Amur Maple", "Deciduous", 0),
                new Taxon( 126, "Sapindaceae", "Acer", "monspessulanum", "", "Montpelier Maple", "Deciduous", 0),
                new Taxon( 128, "Sapindaceae", "Acer", "pseudoplatanus", "", "Sycamore Maple", "Deciduous", 0),
                new Taxon( 129, "Sapindaceae", "Acer", "rubrum", "", "Red Maple", "Deciduous", 0),
                new Taxon( 130, "Betulaceae", "Alnus", "glutinosa", "", "Black alder", "Deciduous", 0),
                new Taxon( 131, "Rosaceae", "Amelanchier", "canadensis", "", "Amelanchier", "Flowering", 0),
                new Taxon( 132, "Ericaceae", "Arbutus", "arnedo", "", "Strawberry tree", "Evergreen", 0),
                new Taxon( 133, "Berberidaceae", "Berberis", "thunbergii", "", "Japanese barberry", "Deciduous", 1),
                new Taxon( 134, "Betulaceae", "Betula", "pendula", "", "Silver birch", "Deciduous", 0),
                new Taxon( 135, "Betulaceae", "Betula", "nigra", "", "River Birch", "Deciduous", 0),
                new Taxon( 136, "Buxaceae", "Buxus", "microphylla", "", "Box, Boxwood", "Evergreen", 0),
                new Taxon( 137, "Theaceae", "Camellia", "japonica", "", "Camellia", "Flowering", 0),
                new Taxon( 138, "Boraginaceae", "Carmona", "microphylla", "", "Fukien tea tree, Ehretia microphylla", "Evergreen", 0),
                new Taxon( 139, "Betulaceae", "Carpinus", "betulus", "", "European hornbeam", "Deciduous", 1),
                new Taxon( 140, "Pinaceae", "Cedrus", "deodara", "", "Deodar cedar", "Coniferous", 1),
                new Taxon( 141, "Pinaceae", "Cedrus", "libani", "", "Lebanon Cedar, Blue Atlas Cedar", "Coniferous", 0),
                new Taxon( 142, "Cannabaceae", "Celtis", "sinensis", "", "Chinese Hackberry", "Deciduous", 0),
                new Taxon( 143, "Rosaceae", "Chaenomeles", "japonica", "", "Flowering Quince", "Flowering", 0),
                new Taxon( 145, "Cupressaceae", "Chamaecyparis", "obtusa", "", "Hinoki Cypress", "Coniferous", 0),
                new Taxon( 146, "Cupressaceae", "Chamaecyparis", "pisifera", "", "Sawara Cypress", "Coniferous", 0),
                new Taxon( 147, "Vitaceae", "Cissus", "antartica", "", "Australian grape vine, Wild Grape, Kangaroo Vine", "Evergreen", 0),
                new Taxon( 148, "Rutaceae", "Citrus", "aurantifolia", "", "", "Evergreen", 0),
                new Taxon( 149, "Cornaceae", "Cornus", "sanguinea", "", "Dogwood", "Deciduous", 0),
                new Taxon( 150, "Anacardiaceae", "Cotinus", "coggygria", "", "Smoke Tree", "Deciduous", 0),
                new Taxon( 151, "Rosaceae", "Cotoneaster", "horizontalis", "", "Rockspray cotoneaster", "Deciduous", 0),
                new Taxon( 152, "Crassulaceae", "Crassula", "ovata", "", "Jades", "Succulent", 0),
                new Taxon( 153, "Rosaceae", "Crataegus", "monogyna", "", "Hawthorn", "Deciduous", 0),
                new Taxon( 154, "Cupressaceae", "Cryptomeria", "japonica", "", "Sugi", "Coniferous", 1),
                new Taxon( 155, "Cupressaceae", "Cupressus", "macrocarpa", "", "Cupressus", "Coniferous", 0),
                new Taxon( 156, "Rosaceae", "Cydonia", "oblonga", "", "Common Quince", "Deciduous", 0),
                new Taxon( 157, "Rosaceae", "Dasiphora", "fruticosa", "", "Shrubby Cinquefoil", "Deciduous", 0),
                new Taxon( 158, "Euphorbiaceae", "Euphorbia", "balsamifera", "", "Wolfsmilk", "Succulent", 0),
                new Taxon( 159, "Celastraceae", "Euonymus", "europaeus", "", "European spindle", "Deciduous", 1),
                new Taxon( 161, "Moraceae", "Ficus", "benghalensis", "", "Banyan", "Tropical", 0),
                new Taxon( 162, "Moraceae", "Ficus", "benjamina", "", "Weeping Fig", "Tropical", 0),
                new Taxon( 163, "Moraceae", "Ficus", "carica", "", "Fig tree, common fig", "Tropical", 0),
                new Taxon( 164, "Moraceae", "Ficus", "microcarpa", "", "Chinese Banyan Fig", "Tropical", 0),
                new Taxon( 165, "Moraceae", "Ficus", "neriifolia", "", "Willow-leaved Fig", "Tropical", 0),
                new Taxon( 166, "Moraceae", "Ficus", "rubiginosa", "", "Port Jackson Fig", "Tropical", 0),
                new Taxon( 167, "Rutaceae", "Fortunella", "hindsii", "", "Dwarf orange", "Evergreen", 0),
                new Taxon( 168, "Oleaceae", "Fraxinus", "excelsior", "", "Ash", "Deciduous", 0),
                new Taxon( 169, "Onagraceae", "Fuchsia", "fulgens", "", "Fuchsia", "Flowering", 0),
                new Taxon( 170, "Rubiaceae", "Gardenia", "jasminoides", "", "Gardenia", "Evergreen", 0),
                new Taxon( 171, "Ginkgoaceae", "Ginkgo", "biloba", "", "Ginkgo", "Deciduous", 2),
                new Taxon( 172, "Proteaceae", "Grevillea", "robusta", "", "Australian Silver Oak", "Evergreen", 0),
                new Taxon( 173, "Araliaceae", "Hedera", "helix", "", "Ivy", "Evergreen", 1),
                new Taxon( 174, "Fabaceae", "Gledista", "triacanthos", "", "Honey Locust", "Deciduous", 0),
                new Taxon( 175, "Malvaceae", "Hibiscus", "syriacus", "", "Hibiscus", "Deciduous", 0),
                new Taxon( 176, "Aquifoliaceae", "Ilex", "aquifolium", "", "Holly", "Evergreen", 0),
                new Taxon( 177, "Bignoniaceae", "Jacaranda", "mimosifolia", "", "Blue jacaranda", "Deciduous", 0),
                new Taxon( 178, "Oleaceae", "Jasminum", "nudiflorum", "", "Winter Jasmine", "Deciduous", 0),
                new Taxon( 179, "Cupressaceae", "Juniperus", "procumbens", "", "Dwarf Japanese Garden Juniper", "Coniferous", 0),
                new Taxon( 180, "Cupressaceae", "Juniperus", "chinensis", "", "Chinese Juniper, Shimpaku", "Coniferous", 0),
                new Taxon( 181, "Cupressaceae", "Juniperus", "squamata", "", "Blue Juniper", "Coniferous", 0),
                new Taxon( 182, "Cupressaceae", "Juniperus", "virginiana", "", "Eastern Juniper", "Coniferous", 0),
                new Taxon( 183, "Lythraceae", "Lagerstroemia", "indica", "", "Crape myrtle, Indian Lilac", "Deciduous", 0),
                new Taxon( 184, "Verbenaceae", "Lantana", "camara", "", "Spanish Flag, West Indian Lantana, Jamaica Mountain Sage, Surinam Tea Plant", "Evergreen", 0),
                new Taxon( 185, "Pinaceae", "Larix", "decidua", "", "Japanese Larch, American Larch, Tamarack", "Coniferous", 1),
                new Taxon( 186, "Oleaceae", "Ligustrum", "vulgare", "", "Privet", "Evergreen", 0),
                new Taxon( 187, "Hamamelidaceae", "Liquidambar", "styraciflua", "", "Sweetgum", "Deciduous", 0),
                new Taxon( 188, "Caprifoliaceae", "Lonicera", "ligustrina", "", "Shrubby Honeysuckles", "Flowering", 0),
                new Taxon( 189, "Magnoliaceae", "Magnolia", "stellata", "", "Star Magnolia", "Evergreen", 0),
                new Taxon( 190, "Rosaceae", "Malus", "sylvestris", "", "Apple/Crabapple", "Flowering", 0),
                new Taxon( 191, "Cupressaceae", "Metasequoia", "glyptostroboides", "", "Dawn Redwood", "Coniferous", 0),
                new Taxon( 192, "Rutaceae", "Murraya", "paniculata", "", "Orange Jasmine, Satinwood", "Evergreen", 0),
                new Taxon( 193, "Myrtaceae", "Myrciaria", "cauliflora", "", "Jaboticaba", "Evergreen", 0),
                new Taxon( 194, "Myrtaceae", "Myrtus", "communis", "", "Myrtle", "Evergreen", 0),
                new Taxon( 195, "Berberidaceae", "Nandina", "domestica", "", "Sacred or Heavenly Bamboo", "Evergreen", 0),
                new Taxon( 196, "Verbenaceae", "Nashia", "inaguensis", "", "Bahama berry", "Evergreen", 0),
                new Taxon( 197, "Nyctaginaceae", "Neea", "buxifolia", "", "", "Tropical", 0),
                new Taxon( 198, "Nothofagaceae", "Nothofagus", "antarctica", "", "Southern beeches", "Deciduous", 1),
                new Taxon( 199, "Oleaceae", "Olea", "europaea", "", "Olive, European olive", "Evergreen", 0),
                new Taxon( 200, "Vitaceae", "Parthenocissus", "quinquefolia", "", "Virginia creeper", "Deciduous", 1),
                new Taxon( 201, "Pinaceae", "Picea", "abies", "", "Spruce", "Coniferous", 1),
                new Taxon( 202, "Pinaceae", "Pinus", "clausa", "", "Sand Pine", "Pine", 0),
                new Taxon( 203, "Pinaceae", "Pinus", "mugo", "", "Mugo Pine, Mountain Pine", "Pine", 0),
                new Taxon( 204, "Pinaceae", "Pinus", "parviflora", "", "Japanese White Pine", "Pine", 0),
                new Taxon( 205, "Pinaceae", "Pinus", "thunbergii", "", "Japanese Black Pine", "Pine", 0),
                new Taxon( 206, "Pinaceae", "Pinus", "virginiana", "", "Virginia Pine", "Pine", 0),
                new Taxon( 207, "Pinaceae", "Pinus", "ponderosa", "", "Western Yellow Pine", "Pine", 0),
                new Taxon( 208, "Podocarpaceae", "Podocarpus", "macrophyllus", "", "Podocarpus, Yew Podocarpus, Kusamaki", "Coniferous", 0),
                new Taxon( 209, "Araliaceae", "Polyscias", "fruticosa", "", "Ming Aralia", "Evergreen", 0),
                new Taxon( 210, "Didiereaceae", "Portulacaria", "afra", "", "Dwarf jade, elephant food, elephant bush", "Succulent", 0),
                new Taxon( 211, "Rosaceae", "Prunus", "serrulata", "", "Japanese Flowering Cherry", "Flowering", 0),
                new Taxon( 212, "Rosaceae", "Prunus", "mume", "", "Flowering Apricot", "Flowering", 0),
                new Taxon( 213, "Lythraceae", "Punica", "granatum", "", "Pomegranate", "Evergreen", 0),
                new Taxon( 214, "Rosaceae", "Pyracantha", "coccinea", "", "Firethorn", "Evergreen", 1),
                new Taxon( 215, "Fagaceae", "Quercus", "rubra", "", "Red oak", "Deciduous", 0),
                new Taxon( 216, "Rosaceae", "Rhaphiolepis", "indica", "", "Indian Hawthorn", "Evergreen", 0),
                new Taxon( 217, "Ericaceae", "Rhododendron", "simsii", "", "Azalea, Satsuki azalea, Kurume azalea", "Evergreen", 0),
                new Taxon( 218, "Papilionaceae", "Robinia", "pseudoacacia", "", "Black Locust", "Deciduous", 0),
                new Taxon( 219, "Rhamnaceae", "Sageretia", "theezans", "", "Chinese Sweet Plum, Pauper\"s Tea", "Evergreen", 0),
                new Taxon( 220, "Araliaceae", "Schefflera", "actinophylla", "", "Australian Umbrella Tree", "Evergreen", 0),
                new Taxon( 221, "Rubiaceae", "Serissa", "foetida", "", "Snow Rose, Japanese Boxthorn, Tree of a Thousand Stars", "Evergreen", 0),
                new Taxon( 222, "Rosaceae", "Sorbus", "aria", "", "Whitebeam", "Deciduous", 2),
                new Taxon( 223, "Myrtaceae", "Syzygium", "australe", "", "Brush cherry, Woolgoolga", "Evergreen", 0),
                new Taxon( 224, "Myrtaceae", "Syzygium", "smithii", "", "Brush cherry", "Evergreen", 0),
                new Taxon( 225, "Tamaricaceae", "Tamarix", "ramosissima", "", "Tamarisk", "Deciduous", 0),
                new Taxon( 226, "Cupressaceae", "Taxodium", "ascendens", "", "Pond cypress", "Coniferous", 0),
                new Taxon( 227, "Cupressaceae", "Taxodium", "distichum", "", "Bald cypress", "Coniferous", 1),
                new Taxon( 228, "Taxaceae", "Taxus", "baccata", "", "Yew", "Coniferous", 5),
                new Taxon( 229, "Pinaceae", "Tsuga", "heterophylla", "", "Western hemlock", "Coniferous", 0),
                new Taxon( 230, "Ulmaceae", "Ulmus", "alata", "", "Winged Elm", "Deciduous", 0),
                new Taxon( 231, "Ulmaceae", "Ulmus", "crassifolia", "", "Cedar Elm", "Deciduous", 0),
                new Taxon( 232, "Ulmaceae", "Ulmus", "minor", "", "Field Elm", "Deciduous", 0),
                new Taxon( 233, "Ulmaceae", "Ulmus", "parvifolia", "", "Chinese Elm", "Deciduous", 0),
                new Taxon( 234, "Papilionaceae", "Wisteria", "floribunda", "", "Japanese Wisteria", "Flowering", 0),
                new Taxon( 235, "Papilionaceae", "Wisteria", "sinensis", "", "Chinese Wisteria", "Flowering", 0),
                new Taxon( 236, "Ulmaceae", "Zelkova", "serrata", "", "Japanese Elm", "Deciduous", 0),
                new Taxon( 239, "Oleaceae", "Forsythia", "x intermedia", "", "Forsythia", "Flowering", 0),
                new Taxon( 240, "Oleaceae", "Forsythia", "x intermedia", "Lynwood", "", "Flowering", 1),
                new Taxon( 241, "Ginkgoaceae", "Ginkgo", "biloba", "Menhir", "", "Deciduous", 1),
                new Taxon( 242, "Hamamelidaceae", "Liquidambar", "styraciflua", "Worplesdon", "", "Deciduous", 1),
                new Taxon( 243, "Hamamelidaceae", "Coryolopsis", "sinensis", "Spring Purple", "", "Deciduous", 1),
                new Taxon( 244, "Salicaceae", "Salix", "udensis", "Sekka", "", "Deciduous", 1),
                new Taxon( 245, "Sapindaceae", "Acer", "palmatum var. dissectum", "", "", "Deciduous", 2),
                new Taxon( 246, "Cupressaceae", "Metasequoia", "glyptostroboides", "Chubby", "", "Coniferous", 1),
                new Taxon( 247, "Oleaceae", "Ligustrum", "sinense", "Sunshine", "Privet", "Evergreen", 2),
                new Taxon( 248, "Aquifoliaceae", "Ilex", "aquifolium", "Alaska", "", "Evergreen", 1),
                new Taxon( 249, "Rosaceae", "Chaenomeles", "speciosa", "Nivalis", "", "Flowering", 1),
                new Taxon( 250, "Cupressaceae", "Cryptomeria", "japonica", "Mushroom", "", "Coniferous", 0),
                new Taxon( 251, "Cupressaceae", "Thuja", "occidentalis", "Teddy", "", "Coniferous", 1),
                new Taxon( 252, "Rosaceae", "Prunus", "incisa", "Kojo-no-mai", "", "Flowering", 3),
                new Taxon( 253, "Rosaceae", "Prunus", "nipponica var. kurilensis", "Brilliant", "Japanese Alpine Cherry", "Flowering", 1),
                new Taxon( 254, "Betulaceae", "Corylus", "avellana", "Twister", "", "Deciduous", 1),
                new Taxon( 255, "Cupressaceae", "Juniperus", "communis", "Hibernica", "", "Coniferous", 1),
                new Taxon( 256, "Rosaceae", "Sorbaria", "sorbifolia", "Sem", "", "Deciduous", 1),
                new Taxon( 257, "Pinaceae", "Cedrus", "deodara", "Golden Horizon", "", "Coniferous", 1),
                new Taxon( 258, "Rosaceae", "Prunus", "lusitanica", "Myrtifolia", "Portuguese laurel", "Evergreen", 1),
                new Taxon( 259, "Escalloniaceae", "Escallonia", "rubra", "Crimson Spire", "", "Evergreen", 1),
                new Taxon( 260, "Grossulariaceae", "Ribes", "sanguineum", "", "Flowering currant", "Flowering", 2),
                new Taxon( 261, "Pinaceae", "Pinus", "sylvestris", "", "Scots pine", "Pine", 1),
                new Taxon( 262, "Rosaceae", "Chaenomeles", "x superba", "Red joy", "", "Flowering", 1),
                new Taxon( 263, "Pinaceae", "Larix", "x marschlinsii", "Domino", "Domino Dunkeld larch", "Coniferous", 1),
                new Taxon( 264, "Pinaceae", "Larix", "kaempferi", "Diana", "Diana Japanese larch", "Coniferous", 2),
                new Taxon( 265, "Nothofagaceae", "Nothofagus", "sp.", "", "", "Deciduous", 0),
                new Taxon( 266, "Pinaceae", "Tsuga", "canadensis", "(dwarf)", "Eastern hemlock", "Coniferous", 1),
                new Taxon( 267, "Pinaceae", "Pinus", "sp.", "", "", "Pine", 1),
                new Taxon( 268, "Ericaceae", "Pieris", "japonica", "Bonfire", "Japanese andromeda", "Flowering", 1),
                new Taxon( 269, "Berberidaceae", "Mahonia", "aquifolium", "", "Oregon grape", "Evergreen", 1),
                new Taxon( 270, "Adoxaceae", "Sambucus", "racemosa", "Lemony Lace", "Red elderberry", "Deciduous", 1),
                new Taxon( 271, "Styracaceae", "Halesia", "monticola", "", "Mountain silverbell", "Flowering", 1),
                new Taxon( 272, "Elaeagnaceae", "Eleagnus", "pungens", "", "Thorny olive", "Evergreen", 1),
                new Taxon( 274, "Fagaceae", "Quercus", "robur", "Atropurpurea", "Purple english oak", "Deciduous", 1),
                new Taxon( 275, "Rosaceae", "Prunus", "x cistena", "", "Purple-leaf sand cherry", "Deciduous", 1),
                new Taxon( 276, "Escalloniaceae", "Escallonia", "rubra var. macrantha", "", "Chilean gum box", "Evergreen", 1),
                new Taxon( 277, "Pinaceae", "Cedrus", "deodara", "aurea", "Deodar", "Coniferous", 1),
                new Taxon( 278, "Pinaceae", "Pinus", "nigra", "", "Black pine", "Pine", 1),
                new Taxon( 279, "Asteraceae", "Olearia", "x macrodonta", "", "Daisy bush", "Evergreen", 1),
                new Taxon( 280, "Aquifoliaceae", "Ilex", "crenata", "", "Japanese holly", "Evergreen", 1),
                new Taxon( 281, "Rosaceae", "Malus", "x scheideckeri", "Red Jade", "Crab apple \"Red jade\"", "Deciduous", 1),
                new Taxon( 282, "Theaceae", "Stewartia", "monodelpha", "", "Tall stewartia", "Deciduous", 1),
                new Taxon( 283, "Fagaceae", "Fagus", "sylvatica", "Pendula", "Weeping beech", "Deciduous", 1),
                new Taxon( 284, "Arecaceae", "Phoenix", "canariensis", "", "Canary Island date palm", "Evergreen", 1),
                new Taxon( 285, "Ericaceae", "Rhododendron", "impeditum", "", "Dwarf purple rhododendron", "Evergreen", 1),
                new Taxon( 286, "Celastraceae", "Euonymus", "phellomanus", "", "Corky spindle", "Deciduous", 1),
                new Taxon( 287, "Araliaceae", "Hedera", "algeriensis", "Gloire de Marengo", "Algerian ivy", "Evergreen", 1),
                new Taxon( 288, "Caprifoliaceae", "Lonicera", "nitida", "Baggesen\"s Gold", "Box honeysuckle", "Evergreen", 1),
                new Taxon( 289, "Taxaceae", "Taxus", "baccata", "Fastigiata aurea", "Irish yew (golden)", "Coniferous", 1),
                new Taxon( 290, "Rosaceae", "Sorbus", "aucuparia", "", "Rowan", "Deciduous", 1),
                new Taxon( 291, "Cupressaceae", "Cupressus", "x leylandii", "", "Leylandii", "Coniferous", 1),
                new Taxon( 292, "Berberidaceae", "Berberis", "x carminea", "Pirate king", "", "Deciduous", 1),
                new Taxon( 293, "Rosaceae", "Cotoneaster", "x suecicus", "Coral Beauty", "", "Evergreen", 1)
        };

        for (int i = 0; i < speciesArrayTmp.length; i++) {
            this.taxonArray.add(speciesArrayTmp[i]);
        }
    }

    public Taxon getTaxon(Integer id) {
        for(int i = 0; i < this.taxonArray.size(); i++) {
            if (this.taxonArray.get(i).id == id) return this.taxonArray.get(id);
        }
        return null;
    }

    public Integer getTaxonIndex(Taxon sp) {
        for(int i = 0; i < this.taxonArray.size(); i++) {
            if (this.taxonArray.get(i).id == sp.id) {
                return i;
            }
        }
        return -1;
    }

    public Taxon addTaxon(Taxon sp) {
        String result = "";
        sp.composeFullName();
        if (sp.id == null) {
            sp.id =  this.generateID();
            this.taxonArray.add(sp);
            result = "Added ";
        } else {
            Integer index = this.getTaxonIndex(sp);
            if (index >= 0) {
                this.taxonArray.set(index, sp);
                result = "Updated ";
            } else {
                sp.id =  this.generateID();
                this.taxonArray.add(sp);
                result = "Added ";
            }
        }
        //logger.info(result + sp.id.toString());
        return sp;
    }

    public Boolean removeTaxon(Taxon sp) {
        Boolean removed = false;
        for(int i = 0; i < this.taxonArray.size(); i++) {
            if (this.taxonArray.get(i).id == sp.id) {
                this.taxonArray.remove(i);
                removed = true;
            }
        }
        return removed;
    }

    public Integer generateID() {
        Integer id =  1;
        for(int i =  0; i < this.taxonArray.size(); i++) {
            if (this.taxonArray.get(i).id > id) id = this.taxonArray.get(i).id;
        }
        return id+1;
    }
}
