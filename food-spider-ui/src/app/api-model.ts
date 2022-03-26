export class UserModel {
    constructor() {
        this.id = '';
        this.emailAddress = '';
        this.firstName = '';
        this.lastName = '';
        this.username = '';
        this.password = '';
        this.orders = null;
        this.restaurants = null;
    }

    public id: string;
    public emailAddress: string;
    public firstName: string;
    public lastName: string;
    public username: string;
    public password: string;
    public orders: any;
    public restaurants: any;
}