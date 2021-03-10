import { Input, OnInit } from "@angular/core";
import { ReprezentantiComisieSauGLModel, NomenclatorValueModel } from "@app/shared";
import { ObjectUtils } from "@app/shared";
import { Message } from "@app/shared";

export abstract class ReprezentantiComisieSauGLTabContent implements OnInit {
	
	@Input()
	public readonly: boolean;

	@Input()
	public inputData: ReprezentantiComisieSauGLTabContentInputData;

	public ngOnInit(): void {
		this.doWhenNgOnInit();
		this.prepareForViewOrEdit();
	}

	protected abstract doWhenNgOnInit(): void;

	protected abstract prepareForViewOrEdit(): void;

	protected abstract isValid(): boolean;
	
	protected abstract populateModel(reprezentantiModel: ReprezentantiComisieSauGLModel): void;

	public abstract getMessages(): Message[];
}

export class ReprezentantiComisieSauGLTabContentInputData {
	public comisieSauGLId: number;
	public comisieSauGL: NomenclatorValueModel;
	public reprezentantiModel: ReprezentantiComisieSauGLModel;
	public comisieSauGLNomenclatorId: number;
	public persoaneNomenclatorId: number;
	public membriCDNomenclatorId;	
	public institutiiNomenclatorId;
	public nrAniValabilitateMandatPresedinteVicepresedinte;
	public nrAniValabilitateMandatMembruCdCoordonator;
	public isCategorieComisie: boolean;
}