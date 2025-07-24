<template>
  <v-container>
    <v-form :disabled="isLoading" @submit.prevent="submitForm">
      <v-row>
        <v-text-field :disabled="editUserDisabled || isLoading" v-model="user" :label="t('domain.user')"></v-text-field>
        <v-btn @click="changeUserEditDisabled">{{ t("domain.notYou") }}</v-btn>
      </v-row>
      <v-select
        v-model="application"
        :label="t('domain.application')"
        :items="items"
        item-title="displayName"
        item-value="name"
      >
     </v-select>
     <v-row justify="center">
       <v-progress-circular v-if="isLoading" indeterminate></v-progress-circular>
       <v-btn :prepend-icon="mdiMagnify" color="primary" :disabled="isLoading" type="submit">{{ t("domain.search") }} </v-btn>
     </v-row>

    </v-form>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
import { useUserStore } from "@/stores/user";
import LicenseUser from "@/types/lizenzliste/LicenseUser";
import SoftwareGroup from "@/types/lizenzliste/SoftwareGroup";
import LicenseRequest from "@/types/forms/LicenseRequest";
import { mdiApps, mdiMagnify } from "@mdi/js";

const {
  items = <SoftwareGroup>[],
  isLoading = false,
} = defineProps<{
  items: SoftwareGroup[];
  isLoading: boolean;
}>();

const model = ref({});

const userStore = useUserStore()

const editUserDisabled = ref(true);

const user = ref<string>('');
const application = ref<string>('');

const emit = defineEmits<{(e: 'submit', payload: LicenseRequest)}>()

onMounted(() => {
  if (userStore.getUser) {
    user.value = userStore.getUser.username;
    console.log(userStore.getUser)
  }
});

function changeUserEditDisabled() : void {
  editUserDisabled.value = !editUserDisabled.value;
};

function submitForm() {
  const req = new LicenseRequest(user.value, application.value);
  emit('submit', req);
};

</script>