import { CategoryEnum, NarrowedFoodItem } from "./api-model";

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
        this.categories = [];
    }

    public adminID: string;
    public name: string;
    public location: string;
    public deliveryZones: string;
    public categories: number[];
}

export class AddFoodItemRequest {

    constructor() {
        this.restaurantID = '';
        this.foodName = '';
        this.foodDescription = '';
        this.price = '';
        this.foodImageLink = '';
        this.categoryEnum = CategoryEnum.BREAKFAST;
    }

    public restaurantID: string;
    public foodName: string;
    public foodDescription: string;
    public price: string;
    public foodImageLink: string;
    public categoryEnum: CategoryEnum;

}

export class AddOrderRequest {
    
    constructor() {
        this.userId = '';
        this.restaurantId = '';
        this.foodItems = new Array<string>();
    }
    
    public userId: string;
    public restaurantId: string;
    public foodItems: Array<string>;
}

export class GetOrdersCountByUserIDResponse {

    constructor() {
        this.pendingOrders = 0;
        this.placedOrders = 0;
    }

    public placedOrders: number;
    public pendingOrders: number;

}

export class ChangeStatusToOrderRequest {

    constructor() {
        this.orderID = '';
        this.status = 0;
    }

    public orderID: string;
    public status: number;
}