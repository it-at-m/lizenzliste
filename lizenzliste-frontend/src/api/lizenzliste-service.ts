import { defaultResponseHandler, getConfig, postConfig } from "@/api/fetch-utils";
import LicenseRequest from "@/types/forms/LicenseRequest";
import LicenseUser from "@/types/lizenzliste/LicenseUser";
import SoftwareGroup from "@/types/lizenzliste/SoftwareGroup";

export function getApplicationList() : Promise<SoftwareGroup[]> {
  return fetch("api/backend-service/software", getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      defaultResponseHandler(err);
    });
}

export function getLicenseHolders(req : LicenseRequest): Promise<LicenseUser[]> {
  return fetch("api/backend-service/list", postConfig(req))
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      defaultResponseHandler(err);
    });
}