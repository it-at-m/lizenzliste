export default class LicenseRequest {
  user: string;
  application: string;

  constructor(user: string, application: string) {
    this.user = user;
    this.application = application;
  }
}
