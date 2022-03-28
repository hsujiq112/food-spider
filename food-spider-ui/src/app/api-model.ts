export class UserModel {
    constructor() {
        this.id = '';
        this.emailAddress = '';
        this.firstName = '';
        this.lastName = '';
        this.username = '';
        this.password = '';
        this.orders = null;
        this.restaurant = new NarrowedResaurant();
    }

    public id: string;
    public emailAddress: string;
    public firstName: string;
    public lastName: string;
    public username: string;
    public password: string;
    public orders: any;
    public restaurant: NarrowedResaurant;
}

export class NarrowedFoodItem {
    constructor() {
        this.id = '';
        this.name = '';
        this.description = '';
        this.price = 0;
        this.category = 0;
        this.imageLink = '';
    }
    public id: string;
    public name: string;
    public description: string;
    public price: number;
    public category: CategoryEnum;
    public imageLink: string;

    public get displayCategory(): string {
        switch(this.category) {
            case CategoryEnum.BREAKFAST:
                return "Breakfast";
            case CategoryEnum.LUNCH:
                return "Lunch";
            case CategoryEnum.DINNER:
                return "Dinner";
            case CategoryEnum.DESSERT:
                return "Dessert";
            case CategoryEnum.BEVERAGES:
                return "Beverages";
            case CategoryEnum.TRENDING:
                return "Trending";
            default:
                return "-";
        }
    }
}

export class NarrowedResaurant {
    constructor() {
        this.id = '';
        this.name = '';
        this.location = '';
        this.deliveryZones = '';
        this.categories = new Array<number>();
    }
    
    public id: string;
    public name: string;
    public location: string;
    public deliveryZones: string;
    public categories: Array<number>;
}

export enum CategoryEnum {
    BREAKFAST,
    LUNCH,
    DINNER,
    DESSERT,
    BEVERAGES,
    TRENDING
}

export enum DisplayTypeEnum {
    ADMIN,
    VIEW,
    ORDER
}

export class DisplayEmitter {

    constructor() {
        this.displayType = DisplayTypeEnum.ADMIN;
        this.restaurantID = '';
    }

    public displayType: DisplayTypeEnum;
    public restaurantID: string;
}