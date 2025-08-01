<template>
  <v-container>
    <v-row class="text-center">
      <v-col class="mb-4">
        <h1 class="text-h3 font-weight-bold mb-3">
          {{ t("views.home.header") }}
        </h1>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col
        cols="12"
        lg="10"
      >
        <request-form
          v-if="items"
          :items="items"
          :isLoading="isLoading"
          @submit="searchLicenseHolders"
        >
        </request-form>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col
        cols="12"
        lg="10"
      >
        <v-table>
          <thead>
            <tr>
              <th>{{ t("components.requestForm.name") }}</th>
              <th>{{ t("components.requestForm.department") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="holder in holders"
              :key="holder.uid"
            >
              <td>{{ holder.displayName }}</td>
              <td>{{ holder.department }}</td>
            </tr>
          </tbody>
        </v-table>
      </v-col>
    </v-row>
    <v-row class="text-center">
      <v-col class="mb-4">
        <p>
          {{ t("views.home.apiGatewayStatus") }}
          <span :class="status">{{ status }}</span>
        </p>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import { checkHealth } from "@/api/health-client";
import {
  getApplicationList,
  getLicenseHolders,
} from "@/api/lizenzliste-service";
import RequestForm from "@/components/RequestForm.vue";
import { useSnackbarStore } from "@/stores/snackbar";
import LicenseRequest from "@/types/forms/LicenseRequest";
import HealthState from "@/types/HealthState";
import LicenseUser from "@/types/lizenzliste/LicenseUser";
import SoftwareGroup from "@/types/lizenzliste/SoftwareGroup";

const { t } = useI18n();

const snackbarStore = useSnackbarStore();
const status = ref("DOWN");

const items = ref<SoftwareGroup[]>([]);
const isLoading = ref(false);
const holders = ref<LicenseUser[]>([]);

onMounted(() => {
  checkHealth()
    .then((content: HealthState) => (status.value = content.status))
    .catch((error) => {
      snackbarStore.showMessage(error);
    });
  getApplicationList().then(
    (content: SoftwareGroup[]) => (items.value = content)
  );
});

function searchLicenseHolders(req: LicenseRequest) {
  isLoading.value = true;
  getLicenseHolders(req)
    .then((rsp) => (holders.value = rsp))
    .finally(() => (isLoading.value = false));
}
</script>

<style scoped>
.UP {
  color: limegreen;
}

.DOWN {
  color: lightcoral;
}
</style>
