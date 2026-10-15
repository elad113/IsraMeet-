// Frontend-only compatibility adapter for the AppDeploy website source.
type ApiResponse={data:any};
const unavailable=async (_path:string,_body?:unknown):Promise<ApiResponse>=>{throw new Error('Backend is not bundled in this APK');};
export const api={get:(path:string)=>unavailable(path),put:(path:string,body?:unknown)=>unavailable(path,body),post:(path:string,body?:unknown)=>unavailable(path,body)};
