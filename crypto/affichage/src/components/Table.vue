<template>
  <table class="table">
    <thead>
      <slot name="columns">
        <tr>
          <th v-for="column in columns" :key="column">{{ column }}</th>
        </tr>
      </slot>
    </thead>
    <tbody>
      <tr v-for="(item, index) in data" :key="index">
        <slot :row="item">
          <td v-for="column in columns" :key="column" v-if="hasValue(item, column)">
            {{ itemValue(item, column) }}
          </td>
          <td v-if="showButton">
            <button class="btn btn-primary" @click="acheter(item)">Acheter</button>
          </td>
        </slot>
      </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  name: 'LTable',
  props: {
    columns: {
      type: Array,
      required: true,
    },
    data: {
      type: Array,
      required: true,
    },
    showButton: {
      type: Boolean,
      default: false, // Utilisez un nom plus explicite que "value"
    },
  },
  methods: {
    hasValue(item, column) {
      return item[column.toLowerCase()] !== undefined; // Correction de la condition
    },
    itemValue(item, column) {
      return item[column.toLowerCase()];
    },
    acheter(item) {
      // Traitez l'élément cliqué ici
      console.log('cyrpto :', item);
      // Vous pouvez également émettre un événement si nécessaire
      this.$emit('button-clicked', item);
    },
  },
};
</script>

<style>
.table {
  width: 100%;
  border-collapse: collapse;
}
.table th,
.table td {
  border: 1px solid #ddd;
  padding: 8px;
}
.table th {
  background-color: #f4f4f4;
  text-align: left;
}
.table tr:hover {
  background-color: #f1f1f1;
}
.btn-primary {
    background-color: #007bff; /* Couleur de fond */
    color: white; /* Couleur du texte */
    padding: 10px 20px; /* Espacement intérieur */
    border: none; /* Supprimer la bordure */
    border-radius: 25px; /* Coins arrondis */
    font-size: 16px; /* Taille de la police */
    font-weight: bold; /* Gras */
    text-transform: uppercase; /* Texte en majuscules */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Ombre */
    transition: all 0.3s ease; /* Transition fluide */
    cursor: pointer; /* Curseur en forme de main */
}

.btn-primary:hover {
    background-color: #0056b3; /* Changement de couleur au survol */
    box-shadow: 0 6px 8px rgba(0, 0, 0, 0.15); /* Ombre plus prononcée au survol */
    transform: translateY(-2px); /* Léger déplacement vers le haut */
}

.btn-primary:active {
    transform: translateY(0); /* Retour à la position initiale lors du clic */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Ombre réduite lors du clic */
}
</style>