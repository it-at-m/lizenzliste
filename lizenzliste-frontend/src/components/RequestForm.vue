<template>
  <v-container>
    <v-form
      :disabled="isLoading"
      @submit.prevent="submitForm"
    >
      <v-row>
        <v-text-field
          :disabled="editUserDisabled || isLoading"
          v-model="user"
          :label="t('components.requestForm.user')"
        ></v-text-field>
        <v-btn @click="changeUserEditDisabled">{{
          t("components.requestForm.notYou")
        }}</v-btn>
      </v-row>
      <v-select
        v-model="application"
        :label="t('components.requestForm.application')"
        :items="items"
        item-title="displayName"
        item-value="name"
      >
      </v-select>
      <v-row justify="center">
        <v-progress-circular
          v-if="isLoading"
          indeterminate
        ></v-progress-circular>
        <v-btn
          :prepend-icon="mdiMagnify"
          color="primary"
          :disabled="isLoading"
          type="submit"
          >{{ t("components.requestForm.search") }}
        </v-btn>
      </v-row>
    </v-form>
  </v-container>
</template>

<script setup lang="ts">
import { mdiMagnify } from "@mdi/js";
import { onMounted, ref, watch } from "vue";
import { useI18n } from "vue-i18n";

import { useUserStore } from "@/stores/user";
import LicenseRequest from "@/types/forms/LicenseRequest";
import SoftwareGroup from "@/types/lizenzliste/SoftwareGroup";

const { t } = useI18n();

interface Props {
  items: SoftwareGroup[];
  isLoading: boolean;
}

const { items, isLoading } = defineProps<Props>();

const userStore = useUserStore();

const editUserDisabled = ref(true);

const user = ref<string | null>(null);

watch(userStore.rawUser(), (e) => (user.value = e?.username ?? null));

const application = ref<string>("");

const emit = defineEmits<(e: "submit", payload: LicenseRequest) => void>();

onMounted(() => {
  user.value = userStore.getUser?.username ?? null;
});

function changeUserEditDisabled(): void {
  editUserDisabled.value = !editUserDisabled.value;
}

function submitForm() {
  if (user.value) {
    const req = new LicenseRequest(user.value, application.value);
    emit("submit", req);
  }
}
</script>
