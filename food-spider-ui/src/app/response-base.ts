import { NarrowedFoodItem, UserModel } from "./api-model";

export class ResponseBase {
    constructor() {
        this.isError = false;
        this.errorMessage = "";
    }

    public isError: boolean;
    public errorMessage: string;
}

export class LoginResponse extends ResponseBase {
    constructor() {
        super(); 
        this.userBase = new UserModel();
        this.isAdmin = false;
    }

    public userBase: UserModel;
    public isAdmin: boolean;
}

export class MenuItemsResponse extends ResponseBase {
    constructor() {
        super();
        this.foodItems = new Array<NarrowedFoodItem>();
    }

    public foodItems: Array<NarrowedFoodItem>;
}