import { UserModel } from "./api-model";

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