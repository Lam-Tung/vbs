import type {InjectionKey} from "vue";
import type {VehicleService} from "@/services/vehicle-service";

export const VehicleServiceKey: InjectionKey<VehicleService> = Symbol('VehicleService');