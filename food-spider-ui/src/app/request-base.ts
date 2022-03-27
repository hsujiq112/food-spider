export class LoginRequest {

    constructor() {
        this.username = '';
        this.password = '';
    }

    public username: string;
    public password: string
}

export class RegisterRequest {

    constructor() {
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.password = "";
        this.emailAddress = "";
        this.isAdmin = false;
    }

    public firstName: string;
    public lastName: string;
    public username: string;
    public password: string;
    public emailAddress: string;
    public isAdmin: boolean;
}

export class AddRestaurantRequest {

    constructor() {
        this.adminID = '';
        this.name = '';
        this.location = '';
        this.deliveryZones = '';
    }

    public adminID: string;
    public name: string;
    public location: string;
    public deliveryZones: string;
}