// Server-side API adapter. AI keys stay in Vercel environment variables.
type ApiResponse={data:any};
async function request(method:string,path:string,body?:unknown):Promise<ApiResponse>{
 const r=await fetch(path,{method,headers:{'Content-Type':'application/json'},...(body===undefined?{}:{body:JSON.stringify(body)})});
 const data=await r.json().catch(()=>({error:'Invalid server response'}));
 if(!r.ok)throw new Error(data?.error||'API request failed');
 return {data};
}
export const api={get:(path:string)=>request('GET',path),put:(path:string,body?:unknown)=>request('PUT',path,body),post:(path:string,body?:unknown)=>request('POST',path,body)};
