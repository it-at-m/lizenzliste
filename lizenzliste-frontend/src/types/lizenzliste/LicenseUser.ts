export default class LicenseUser {
  uid: string;
  displayName: string | null;
  department: string | null;

  constructor(uid: string) {
    this.uid = uid;
    this.displayName = null;
    this.department = null;
  }
}
