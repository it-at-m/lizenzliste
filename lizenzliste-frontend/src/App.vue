<template>
  <v-app>
    <the-snackbar />
    <v-app-bar color="primary">
      <v-row align="center">
        <v-col
          cols="3"
          class="d-flex align-center justify-start"
        >
          <v-app-bar-nav-icon @click.stop="toggleDrawer()" />
          <router-link to="/">
            <v-toolbar-title class="font-weight-bold">
              <span class="text-white">{{ t("app.name.part1") }}</span>
              <span class="text-secondary">{{ t("app.name.part2") }}</span>
              <span class="text-white">{{ t("app.name.part3") }}</span>
            </v-toolbar-title>
          </router-link>
        </v-col>
        <v-col
          cols="6"
          class="d-flex align-center justify-center"
        >
        </v-col>
        <v-col
          cols="3"
          class="d-flex align-center justify-end"
        >
          <app-switcher
            v-if="appswitcherBaseUrl"
            :base-url="appswitcherBaseUrl"
            :tags="['global']"
            :icon="mdiApps"
          />
          <v-btn
            variant="text"
            icon
          >
            <ad2-image-avatar
              v-if="userStore.getUser !== null"
              :username="userStore.getUser.username"
            />
          </v-btn>
        </v-col>
      </v-row>
    </v-app-bar>
    <v-navigation-drawer v-model="drawer">
      <v-list>
        <v-list-item :to="{ name: ROUTES_GETSTARTED }">
          <v-list-item-title>
            {{ t("views.getStarted.navText") }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-main>
      <v-container fluid>
        <router-view v-slot="{ Component }">
          <v-fade-transition mode="out-in">
            <component :is="Component" />
          </v-fade-transition>
        </router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { mdiApps } from "@mdi/js";
import { AppSwitcher } from "@muenchen/appswitcher-vue";
import { useToggle } from "@vueuse/core";
import { onMounted } from "vue";
import { useI18n } from "vue-i18n";

import { getUser } from "@/api/user-client";
import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { APPSWITCHER_URL, ROUTES_GETSTARTED } from "@/constants";
import { useUserStore } from "@/stores/user";
import User, { UserLocalDevelopment } from "@/types/User";

const { t } = useI18n();

const appswitcherBaseUrl = APPSWITCHER_URL;

const userStore = useUserStore();
const [drawer, toggleDrawer] = useToggle();

onMounted(() => {
  loadUser();
});

/**
 * Loads UserInfo from the backend and sets it in the store.
 */
function loadUser(): void {
  getUser()
    .then((user: User) => userStore.setUser(user))
    .catch(() => {
      // No user info received, so fallback
      if (import.meta.env.DEV) {
        userStore.setUser(UserLocalDevelopment());
      } else {
        userStore.setUser(null);
      }
    });
}
</script>
