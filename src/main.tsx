import { StrictMode, Component, type ErrorInfo, type ReactNode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

class StartupGuard extends Component<{children:ReactNode},{failed:boolean;detail:string}> {
  state={failed:false,detail:''};
  static getDerivedStateFromError(error:Error){return {failed:true,detail:error?.message||'Unknown startup error'};}
  componentDidCatch(error:Error,info:ErrorInfo){console.error('CodeQuest startup failed',error,info.componentStack);}
  render(){if(this.state.failed)return <main style={{minHeight:'100vh',padding:'28px',boxSizing:'border-box',background:'linear-gradient(145deg,#160c32,#211047 58%,#0f2045)',color:'#fff',fontFamily:'system-ui,sans-serif'}}><div style={{maxWidth:560,margin:'12vh auto',padding:24,borderRadius:24,border:'1px solid #ffffff30',background:'#21183fee'}}><div style={{fontSize:36}}>🛠️</div><h1 style={{marginBottom:8}}>CodeQuest needs a quick reset</h1><p style={{color:'#ded5f7',lineHeight:1.6}}>The app hit a startup issue. Your saved progress is kept. Try reloading first; if it continues, clear only the old app data from Settings.</p><pre style={{whiteSpace:'pre-wrap',overflowWrap:'anywhere',color:'#ffb7c6',fontSize:12}}>{this.state.detail}</pre><button onClick={()=>location.reload()} style={{border:0,borderRadius:12,padding:'12px 18px',fontWeight:800,background:'#a477ff',color:'white'}}>Reload CodeQuest</button></div></main>;return this.props.children;}
}

createRoot(document.getElementById('root')!).render(<StrictMode><StartupGuard><App /></StartupGuard></StrictMode>);
