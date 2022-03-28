import { NarrowedFoodItem, NarrowedResaurant, UserModel } from "./api-model";

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
        this.categories = new Array<number>();
    }

    public foodItems: Array<NarrowedFoodItem>;
    public categories: Array<number>;
}

export class GetRestaurantByAdminIDResponse extends ResponseBase {

    constructor() {
        super();
        this.narrowedRestaurant = new NarrowedResaurant();
    }

    public narrowedRestaurant: NarrowedResaurant;
}

export class AddRestaurantResponse extends ResponseBase {

    constructor() {
        super();
        this.restaurant = new NarrowedResaurant();
    }

    public restaurant: NarrowedResaurant;
}

export class GetRestaurantsResponse extends ResponseBase {

    constructor() {
        super();
        this.restaurants = new Array<NarrowedResaurant>();
    }

    public restaurants: Array<NarrowedResaurant>;
}